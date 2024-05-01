import FlowGraph from "./FlowGraph.js";
import {
  MethodNode,
  Joinpoint,
  Program,
  Statement,
  ReturnStatement,
  Label,
  Goto,
  IfComparison,
  IfComparisonWithZero,
  Switch,
  LabelReference,
  PackedSwitch,
  SparseSwitch,
  SparseSwitchElement,
} from "../../../../Joinpoints.js";
import Query from "lara-js/api/weaver/Query.js";
import UnknownInstructionNode from "./node/instruction/UnknownInstructionNode.js";
import FlowNode from "./node/FlowNode.js";
import InstructionNode from "./node/instruction/InstructionNode.js";
import { NodeBuilder } from "../graph/Node.js";
import StatementNode from "./node/instruction/StatementNode.js";
import FunctionEntryNode from "./node/instruction/FunctionEntryNode.js";
import FunctionExitNode from "./node/instruction/FunctionExitNode.js";
import ReturnNode from "./node/instruction/ReturnNode.js";
import LabelNode from "./node/instruction/LabelNode.js";
import GotoNode from "./node/instruction/GotoNode.js";
import ConditionNode from "./node/condition/ConditionNode.js";
import SwitchNode from "./node/instruction/SwitchNode.js";

export default class FlowGraphGenerator {
  #$jp: Joinpoint;
  #graph: FlowGraph.Class;
  #temporaryNodes: UnknownInstructionNode.Class[];

  constructor($jp: Program | MethodNode, graph: FlowGraph.Class) {
    this.#$jp = $jp;
    this.#graph = graph;
    this.#temporaryNodes = [];
  }

  build(): FlowGraph.Class {
    if (this.#$jp instanceof Program) {
      for (const $function of Query.searchFrom(this.#$jp, "methodNode")) {
        this.#processFunction($function as MethodNode);
      }
    } else if (this.#$jp instanceof MethodNode) {
      this.#processFunction(this.#$jp);
    }

    for (const node of this.#temporaryNodes) {
      node.removeFromFlow();
      node.remove();
    }

    return this.#graph;
  }

  #processJp(
    $jp: Joinpoint,
    context: ProcessJpContext,
  ): [FlowNode.Class, InstructionNode.Class?] {
    if (context.preprocessedStatementStack.length > 0) {
      const [head, tail] = context.preprocessedStatementStack.pop()!;
      return [head, tail];
    }

    if ($jp instanceof Label) {
      return this.#processLabelStmt($jp, context);
    } else if ($jp instanceof Switch) {
      return this.#processSwitch($jp, context);
    } else if (
      $jp instanceof IfComparison ||
      $jp instanceof IfComparisonWithZero
    ) {
      return this.#processIf($jp, context);
    } else if ($jp instanceof Goto) {
      return this.#processGoto($jp, context);
      // } else if ($jp instanceof ReturnStatement) {
      //   return this.#addOutwardsJump(
      //       new ReturnNode.Builder($jp),
      //       context.returnNode!,
      //   );
    } else if ($jp instanceof Statement) {
      return this.#addInstruction(new StatementNode.Builder($jp));
    } else {
      throw new Error(
        `Cannot build graph for joinpoint "${$jp.joinPointType}"`,
      );
    }
  }

  #processFunction(
    $jp: MethodNode,
  ): [FunctionEntryNode.Class, FunctionExitNode.Class?] {
    const returnNode = this.#createTemporaryNode($jp);

    const context = {
      returnNode,
      labels: new Map(),
      preprocessedStatementStack: [],
    };

    const body = $jp.children.map((child) => {
      const [head, tail] = this.#processJp(child, context);
      return [head, tail ? [tail] : []] as [
        FlowNode.Class,
        InstructionNode.Class[],
      ];
    });

    for (let i = 0; i < body.length - 1; i++) {
      const [head, tail] = body[i];
      const [nextHead, nextTail] = body[i + 1];

      for (const tailNode of tail) {
        tailNode.nextNode = nextHead;
      }
    }

    let functionTail: InstructionNode.Class[] = [];
    const bodyTail = body[body.length - 1][1];
    if (bodyTail !== undefined) {
      returnNode.insertSubgraphBefore(body[body.length - 1][0], bodyTail);
      functionTail = [returnNode];
    } else if (returnNode.incomers.length > 0) {
      functionTail = [returnNode];
    }

    const bodyHead = body[0][0];

    return this.#graph.addFunction($jp, bodyHead, functionTail);
  }

  #processIf(
    $jp: IfComparison | IfComparisonWithZero,
    context: ProcessJpContext,
  ): [ConditionNode.Class, InstructionNode.Class?] {
    const $iftrue = $jp.label.decl;
    const $iffalse = $jp.nextStatement;

    if (!context.labels.has($iftrue.name)) {
      this.#processLabelStmt($iftrue, context);
    }

    let ifTrueHead: FlowNode.Class;
    const label = context.labels.get($jp.label.name);
    if (label !== undefined) {
      ifTrueHead = label;
    } else {
      const trueNode = this.#createTemporaryNode();
      ifTrueHead = trueNode;
    }

    let ifFalseHead: FlowNode.Class;
    let ifFalseTail: InstructionNode.Class | undefined;

    [ifFalseHead, ifFalseTail] = this.#processJp($iffalse, context);
    context.preprocessedStatementStack.push([ifFalseHead, ifFalseTail]);

    return [this.#graph.addCondition($jp, ifTrueHead, ifFalseHead)];
  }

  #processSwitch(
    $jp: Switch,
    context: ProcessJpContext,
  ): [SwitchNode.Class, InstructionNode.Class?] {
    const $labelRef = $jp.getChild(1);
    if (!($labelRef instanceof LabelReference)) {
      throw new Error("Switch statement must include a label reference");
    }
    const $switchDecl = ($labelRef as LabelReference).decl.nextStatement;
    const defaultCase = $jp.nextStatement;

    const node = this.#graph
      .addNode()
      .init(new SwitchNode.Builder($jp))
      .as(SwitchNode.Class);

    const $children = $switchDecl.children;
    let previousCase: ConditionNode.Class | undefined = undefined;

    const childrenRefs: LabelReference[] = [];

    if ($switchDecl instanceof PackedSwitch) {
      for (const childRef of $children) {
        if (!(childRef instanceof LabelReference)) {
          throw new Error(
            "Packed switch directive children must be label references",
          );
        }

        if (!context.labels.has(childRef.name)) {
          this.#processLabelStmt(childRef.decl, context);
        }

        childrenRefs.push(childRef as LabelReference);
      }
    } else if ($switchDecl instanceof SparseSwitch) {
      for (const element of $children) {
        if (!(element instanceof SparseSwitchElement)) {
          throw new Error(
            "Sparse switch directive children must be sparse switch elements",
          );
        }

        const childRef = element.label;
        if (!(childRef instanceof LabelReference)) {
          throw new Error(
            "Sparse switch element must contain a label reference",
          );
        }

        if (!context.labels.has(childRef.name)) {
          this.#processLabelStmt(childRef.decl, context);
        }

        childrenRefs.push(childRef as LabelReference);
      }
    }

    for (const child of childrenRefs) {
      const label = context.labels.get(child.name);

      if (label !== undefined) {
        const currentCase = this.#graph.addCondition(
          child as LabelReference,
          label,
          label, // False node doesn't matter for now, since it will change
        );

        if (previousCase === undefined) {
          node.nextNode = currentCase;
        } else {
          previousCase.falseNode = currentCase;
        }

        // for (const incomer of label.incomers) {
        //   incomer.target = label;
        // }

        previousCase = currentCase;
      }
    }

    const preProcessedStatement = this.#processJp(defaultCase, context);
    context.preprocessedStatementStack.push(preProcessedStatement);

    if (previousCase === undefined) {
      node.nextNode = preProcessedStatement[0];
    } else {
      previousCase.falseNode = preProcessedStatement[0];
    }

    return [node];
  }

  #processLabelStmt(
    $jp: Label,
    context: ProcessJpContext,
  ): [LabelNode.Class, LabelNode.Class] {
    if (context.labels.has($jp.name)) {
      const label = context.labels.get($jp.name);
      if (label !== undefined) {
        return [label, label];
      }
    }

    const node = this.#graph
      .addNode()
      .init(new LabelNode.Builder($jp))
      .as(LabelNode.Class);

    context.labels.set($jp.name, node);

    return [node, node];
  }

  #processGoto($jp: Goto, context: ProcessJpContext): [GotoNode.Class] {
    const node = this.#graph
      .addNode()
      .init(new GotoNode.Builder($jp))
      .as(GotoNode.Class);

    if (!context.labels.has($jp.label.name)) {
      this.#processLabelStmt($jp.label.decl, context);
    }

    const label = context.labels.get($jp.label.name);
    if (label !== undefined) {
      this.#connectArbitraryJump(node, label);
    }

    return [node];
  }

  #createTemporaryNode($jp?: Joinpoint): UnknownInstructionNode.Class {
    const node = this.#graph
      .addNode()
      .init(new UnknownInstructionNode.Builder($jp))
      .as(UnknownInstructionNode.Class);
    this.#temporaryNodes.push(node);
    return node;
  }

  #addInstruction(
    builder: NodeBuilder<InstructionNode.Data, InstructionNode.ScratchData>,
  ): [InstructionNode.Class, InstructionNode.Class] {
    const node = this.#graph.addNode().init(builder).as(InstructionNode.Class);
    return [node, node];
  }

  #addOutwardsJump(
    builder: NodeBuilder<InstructionNode.Data, InstructionNode.ScratchData>,
    jumpTo: FlowNode.Class,
  ): [InstructionNode.Class] {
    const node = this.#graph.addNode().init(builder).as(InstructionNode.Class);

    const exitNode: InstructionNode.Class = node;
    // let $currentJp = node.jp!;

    // while ($currentJp.astId !== jumpTo.jp!.astId) {
    //     if ($currentJp instanceof Scope) {
    //         const endScope = this.#graph
    //             .addNode()
    //             .init(new ScopeEndNode.Builder($currentJp))
    //             .as(ScopeEndNode.Class);

    //         exitNode.nextNode = endScope;
    //         exitNode = endScope;
    //     }

    //     $currentJp = $currentJp.parent;
    // }

    exitNode.nextNode = jumpTo;

    return [node];
  }

  #connectArbitraryJump(from: InstructionNode.Class, to: FlowNode.Class) {
    // const fromScopes = this.#getScopeList(from.jp!);
    // const toScopes = this.#getScopeList(to.jp!);
    // let fromScopesIdx = fromScopes.length - 1;
    // let toScopesIdx = toScopes.length - 1;

    // while (
    //     toScopesIdx >= 0 &&
    //     fromScopesIdx >= 0 &&
    //     toScopes[toScopesIdx].astId === fromScopes[fromScopesIdx].astId
    // ) {
    //     toScopesIdx--;
    //     fromScopesIdx--;
    // }

    // let exitNode: InstructionNode.Class = from;

    // for (let i = 0; i <= fromScopesIdx; i++) {
    //     const endScope = this.#graph
    //         .addNode()
    //         .init(new ScopeEndNode.Builder(fromScopes[i]))
    //         .as(ScopeEndNode.Class);

    //     exitNode.nextNode = endScope;
    //     exitNode = endScope;
    // }

    // for (let i = toScopesIdx; i >= 0; i--) {
    //     const startScope = this.#graph
    //         .addNode()
    //         .init(new ScopeStartNode.Builder(toScopes[i]))
    //         .as(ScopeStartNode.Class);

    //     exitNode.nextNode = startScope;
    //     exitNode = startScope;
    // }

    // exitNode.nextNode = to;

    from.nextNode = to;
  }
}

interface ProcessJpContext {
  returnNode: FlowNode.Class;
  labels: Map<string, LabelNode.Class>;
  preprocessedStatementStack: Array<[FlowNode.Class, InstructionNode.Class?]>;
}

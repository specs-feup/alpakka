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
    // if ($jp instanceof MethodNode) {
    //     return this.#processFunction($jp);
    // } else if ($jp instanceof Scope) {
    //     return this.#processScope($jp, context);
    // } else if ($jp instanceof WrapperStmt) {
    //     if ($jp.kind === "comment") {
    //         return this.#addInstruction(
    //             new CommentNode.Builder($jp.content as Comment),
    //         );
    //     } else if ($jp.kind === "pragma") {
    //         return this.#addInstruction(
    //             new PragmaNode.Builder($jp.content as Pragma),
    //         );
    //     } else {
    //         throw new Error(
    //             `Cannot build graph for "${$jp.joinPointType}:${$jp.kind}"`,
    //         );
    //     }

    if (context.preprocessedStatementStack.length > 0) {
      const [head, tail] = context.preprocessedStatementStack.pop()!;
      return [head, tail];
    }

    if ($jp instanceof Label) {
      return this.#processLabelStmt($jp, context);
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
      console.log($jp.joinPointType);
      throw new Error(
        `Cannot build graph for joinpoint "${$jp.joinPointType}"`,
      );
    }

    // if ($jp instanceof DeclStmt) {
    //     return this.#processVarDecl($jp);
    // } else if ($jp instanceof EmptyStmt) {
    //     return this.#addInstruction(new EmptyStatementNode.Builder($jp));
    // } else if ($jp instanceof ExprStmt) {
    //     return this.#addInstruction(new ExpressionNode.Builder($jp.expr));
    // } else if ($jp instanceof If) {
    //     return this.#processIf($jp, context);
    // // } else if ($jp instanceof Loop) {
    // //     return this.#processLoop($jp, context);
    // } else if ($jp instanceof Switch) {
    //     return this.#processSwitch($jp, context);
    // } else if ($jp instanceof Case) {
    //     // Case nodes will be processed by the Switch
    //     // Marking them as a temporary is enough
    //     const node = this.#createTemporaryNode($jp);
    //     if ($jp.isDefault) {
    //         context.defaultCase = node;
    //     } else {
    //         context.caseNodes?.push(node);
    //     }
    //     return [node, node];
    // } else if ($jp instanceof ReturnStmt) {
    //     return this.#addOutwardsJump(
    //         new ReturnNode.Builder($jp),
    //         context.returnNode!,
    //     );
    // // } else if ($jp instanceof Break) {
    // //     return this.#addOutwardsJump(new BreakNode.Builder($jp), context.breakNode!);
    // // } else if ($jp instanceof Continue) {
    // //     return this.#addOutwardsJump(
    // //         new ContinueNode.Builder($jp),
    // //         context.continueNode!,
    // //     );
    // } else if ($jp instanceof Label) {
    //     return this.#processLabelStmt($jp, context);
    // } else if ($jp instanceof Goto) {
    //     return this.#processGoto($jp, context);
    // } else {
    //     // TODO maybe be silent when inside recursive calls?
    //     throw new Error(`Cannot build graph for joinpoint "${$jp.joinPointType}"`);
    // }
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

  // // #processScope(
  // //     $jp: Scope,
  // //     context: ProcessJpContext,
  // // ): [ScopeStartNode.Class, ScopeEndNode.Class?] {
  // //     const subGraphs = $jp.children.map((child) => {
  // //         const [head, tail] = this.#processJp(child, context);
  // //         return [head, tail ? [tail] : []] as [
  // //             FlowNode.Class,
  // //             InstructionNode.Class[],
  // //         ];
  // //     });
  // //     return this.#graph.addScope($jp, subGraphs);
  // // }

  // #processVarDecl($jp: DeclStmt): [VarDeclarationNode.Class, VarDeclarationNode.Class] {
  //     if ($jp.decls.length === 0) {
  //         throw new Error("Empty declaration statement");
  //     }

  //     let head: VarDeclarationNode.Class | undefined;
  //     let tail: VarDeclarationNode.Class | undefined;

  //     for (const $decl of $jp.decls) {
  //         if ($decl instanceof Vardecl) {
  //             const node = this.#graph
  //                 .addNode()
  //                 .init(new VarDeclarationNode.Builder($decl))
  //                 .as(VarDeclarationNode.Class);

  //             if (head === undefined) {
  //                 head = node;
  //             }

  //             if (tail !== undefined) {
  //                 tail.nextNode = node;
  //             }

  //             tail = node;
  //         } else {
  //             throw new Error("Unsupported declaration type");
  //         }
  //     }

  //     return [head!, tail!];
  // }

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

  // // #processLoop(
  // //     $jp: Loop,
  // //     context: ProcessJpContext,
  // // ): [FlowNode.Class, InstructionNode.Class] {
  // //     const continueNode = this.#createTemporaryNode($jp);
  // //     const breakNode = this.#createTemporaryNode($jp);
  // //     const [bodyHead, bodyTail] = this.#processScope($jp.body, {
  // //         ...context,
  // //         breakNode,
  // //         continueNode,
  // //     });

  // //     const node = this.#graph.addLoop(
  // //         $jp,
  // //         bodyHead,
  // //         bodyTail ? [bodyTail] : [],
  // //         breakNode,
  // //     );

  // //     let head: FlowNode.Class;
  // //     if ($jp.kind === "for") {
  // //         const [, init] = this.#processJp($jp.init, context);
  // //         const [, step] = this.#processJp($jp.step, context);

  // //         if (init === undefined) {
  // //             throw new Error("Init must be an instruction node");
  // //         }

  // //         if (step === undefined) {
  // //             throw new Error("Step must be an instruction node");
  // //         }

  // //         continueNode.insertBefore(init);
  // //         node.insertBefore(step);

  // //         head = init;
  // //     } else if ($jp.kind == "dowhile") {
  // //         head = bodyHead;
  // //     } else if ($jp.kind == "while") {
  // //         head = continueNode;
  // //     } else {
  // //         throw new Error(`Unsupported loop kind "${$jp.kind}"`);
  // //     }

  // //     node.insertBefore(continueNode);

  // //     return [head, breakNode];
  // // }

  // #processSwitch(
  //     $jp: Switch,
  //     context: ProcessJpContext,
  // ): [SwitchNode.Class, ScopeEndNode.Class?] {

  //     // We know child 1 is a Label to a switch but we don't know the type sparse or packed

  //     // We also know the default case is the next statement

  //     const $labelRef = $jp.getChild(1);
  //     if (!($labelRef instanceof LabelReference)) {
  //         throw new Error("Switch statement must include a label reference");
  //     }
  //     const $switchDecl = ($labelRef as LabelReference).decl.nextStatement;
  //     const $defaultCase = $jp.nextStatement;

  //     if ($switchDecl instanceof PackedSwitch) {
  //     }
  //     else if ($switchDecl instanceof SparseSwitch) {
  //     }

  //     const $body = $jp.getChild(1);
  //     if (!($body instanceof Scope)) {
  //         throw new Error("Switch body must be a scope");
  //     }
  //     const breakNode = this.#createTemporaryNode($body);
  //     const caseNodes: UnknownInstructionNode.Class[] = [];
  //     const innerContext = { ...context, breakNode, caseNodes };
  //     const [bodyHead, bodyTail] = this.#processScope($body, innerContext);
  //     const defaultCase = innerContext.defaultCase;

  //     const node = this.#graph
  //         .addNode()
  //         .init(new SwitchNode.Builder($jp))
  //         .as(SwitchNode.Class);

  //     bodyHead.insertBefore(node);

  //     let previousCase: ConditionNode.Class | undefined = undefined;
  //     for (const tempCaseNode of caseNodes) {
  //         const currentCase = this.#graph.addCondition(
  //             tempCaseNode.jp as Case,
  //             tempCaseNode.nextNode!,
  //             tempCaseNode, // False node doesn't matter for now, since it will change
  //         );

  //         if (previousCase === undefined) {
  //             bodyHead.nextNode = currentCase;
  //         } else {
  //             previousCase.falseNode = currentCase;
  //         }

  //         for (const incomer of tempCaseNode.incomers) {
  //             incomer.target = tempCaseNode.nextNode!;
  //         }

  //         previousCase = currentCase;
  //     }

  //     if (defaultCase !== undefined) {
  //         const currentCase = this.#graph.addCondition(
  //             defaultCase.jp as Case,
  //             defaultCase.nextNode!,
  //             defaultCase, // False node doesn't matter for now, since it will change
  //         );

  //         if (previousCase === undefined) {
  //             bodyHead.nextNode = currentCase;
  //         } else {
  //             previousCase.falseNode = currentCase;
  //         }

  //         for (const incomer of defaultCase.incomers) {
  //             incomer.target = defaultCase.nextNode!;
  //         }

  //         previousCase = currentCase;
  //     }

  //     let scopeEnd = bodyTail;
  //     if (scopeEnd === undefined) {
  //         if (breakNode.incomers.length === 0) {
  //             return [node];
  //         }

  //         scopeEnd = this.#graph
  //             .addNode()
  //             .init(new ScopeEndNode.Builder($body))
  //             .as(ScopeEndNode.Class);

  //         breakNode.nextNode = scopeEnd;
  //     }

  //     scopeEnd.insertBefore(breakNode);

  //     if (previousCase === undefined) {
  //         bodyHead.nextNode = scopeEnd;
  //     } else {
  //         previousCase.falseNode = scopeEnd;
  //     }

  //     return [node, scopeEnd];
  // }

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

  //     // #getScopeList($jp: Joinpoint): Scope[] {
  //     //     const result: Scope[] = [];

  //     //     while (true) {
  //     //         if ($jp instanceof Scope) {
  //     //             result.push($jp);
  //     //         }
  //     //         if ($jp.hasParent) {
  //     //             $jp = $jp.parent;
  //     //         } else {
  //     //             break;
  //     //         }
  //     //     }

  //     //     return result;
  //     // }
}

interface ProcessJpContext {
  returnNode: FlowNode.Class;
  labels: Map<string, LabelNode.Class>;
  preprocessedStatementStack: Array<[FlowNode.Class, InstructionNode.Class?]>;
  continueNode?: FlowNode.Class;
  breakNode?: FlowNode.Class;
  caseNodes?: UnknownInstructionNode.Class[];
  defaultCase?: UnknownInstructionNode.Class;
}

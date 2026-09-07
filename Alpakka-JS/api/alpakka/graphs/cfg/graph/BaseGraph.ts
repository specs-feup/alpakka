import cytoscape from "cytoscape";
import * as Graph from "./Graph.ts";
import type { GraphBuilder, GraphConstructor, GraphTransformation, GraphTypeGuard,  } from "./Graph.ts";
import * as BaseNode from "./BaseNode.ts";
import * as BaseEdge from "./BaseEdge.ts";
import type { JavaClasses } from "@specs-feup/lara/api/lara/util/JavaTypes.ts";
import DotFormatter from "../dot/DotFormatter.ts";
import Io from "@specs-feup/lara/api/lara/Io.ts";

export class Class<
  D extends Data = Data,
  S extends ScratchData = ScratchData,
> {
  #graph: cytoscape.Core;

  // _d and _sd are a hack to force typescript to typecheck
  // D and S in .as() method.
  constructor(graph: cytoscape.Core, _d: D = {} as any, _sd: S = {} as any) {
    this.#graph = graph;
  }

  get data(): D {
    return this.#graph.data();
  }

  get scratchData(): S {
    return this.#graph.scratch(Graph.scratchNamespace);
  }

  addNode(id?: string): BaseNode.Class {
    const newNode = this.#graph.add({
      group: "nodes",
      data: { id },
    });
    return new BaseNode.Class(this, newNode);
  }

  addEdge(
    source: BaseNode.Class,
    target: BaseNode.Class,
    id?: string,
  ): BaseEdge.Class {
    const newEdge = this.#graph.add({
      group: "edges",
      data: { id, source: source.id, target: target.id },
    });
    return new BaseEdge.Class(this, newEdge);
  }

  getNodeById(id: string): BaseNode.Class | undefined {
    const node = this.#graph.getElementById(id);
    if (node.isNode()) {
      return new BaseNode.Class(this, node);
    }

    return undefined;
  }

  getEdgeById(id: string): BaseEdge.Class | undefined {
    const edge = this.#graph.getElementById(id);
    if (edge.isEdge()) {
      return new BaseEdge.Class(this, edge);
    }

    return undefined;
  }

  // TODO
  get nodes(): BaseNode.Class[] {
    return this.#graph.nodes().map((node) => new BaseNode.Class(this, node));
  }

  // TODO
  get edges(): BaseEdge.Class[] {
    return this.#graph.edges().map((edge) => new BaseEdge.Class(this, edge));
  }

  is<D2 extends Data, S2 extends ScratchData>(
    guard: GraphTypeGuard<D2, S2>,
  ): this is Class<D2, S2> {
    const data = this.data;
    const scratchData = this.scratchData;
    const result =
      guard.isDataCompatible(data) &&
      guard.isScratchDataCompatible(scratchData);

    // Have typescript statically check that the types are correct
    // in the implementation of this function.
    void (result && (data satisfies D2) && (scratchData satisfies S2));

    return result;
  }

  as<G extends Class<D, S>>(
    GraphType: GraphConstructor<D, S, G>,
  ): G {
    return new GraphType(this.#graph, this.data, this.scratchData);
  }

  init<D2 extends Data, S2 extends ScratchData>(
    builder: GraphBuilder<D2, S2>,
  ): Class<D2, S2> {
    const initedData = builder.buildData(this.data);
    const initedScratchData = builder.buildScratchData(this.scratchData);
    this.#graph.data(initedData);
    this.#graph.scratch(Graph.scratchNamespace, initedScratchData);
    return new Class(this.#graph, initedData, initedScratchData);
  }

  apply(transformation: GraphTransformation): this {
    transformation.apply(this);
    return this;
  }

  toDot(dotFormatter: DotFormatter, label?: string): string {
    return dotFormatter.format(this, label);
  }

  toDotFile(
    dotFormatter: DotFormatter,
    filename: string,
    label?: string,
  ): JavaClasses.File {
    return Io.writeFile(filename, this.toDot(dotFormatter, label));
  }

  toCy(): cytoscape.Core {
    return this.#graph;
  }
}

export class Builder implements GraphBuilder<Data, ScratchData> {
  buildData(data: Data): Data {
    return data;
  }

  buildScratchData(scratchData: ScratchData): ScratchData {
    return scratchData;
  }
}

export const TypeGuard: GraphTypeGuard<Data, ScratchData> = {
  isDataCompatible(_data: Data): _data is Data {
    return true;
  },

  isScratchDataCompatible(
    _sData: ScratchData,
  ): _sData is ScratchData {
    return true;
  },
};

export interface Data {}

export interface ScratchData {}

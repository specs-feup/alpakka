import * as BaseGraph from "./BaseGraph.ts";
import * as BaseNode from "./BaseNode.ts";
import type { EdgeBuilder, EdgeConstructor, EdgeTypeGuard } from "./Edge.ts";
import * as Graph from "./Graph.ts";
import cytoscape from "cytoscape";

export class Class<
  D extends Data = Data,
  S extends ScratchData = ScratchData,
> {
  #graph: BaseGraph.Class;
  #edge: cytoscape.EdgeSingular;

  // _d and _sd are a hack to force typescript to typecheck
  // D and S in .as() method.
  constructor(
    graph: BaseGraph.Class,
    edge: cytoscape.EdgeSingular,
    _d: D = {} as any,
    _sd: S = {} as any,
  ) {
    this.#graph = graph;
    this.#edge = edge;
  }

  get data(): D {
    return this.#edge.data();
  }

  get scratchData(): S {
    return this.#edge.scratch(Graph.scratchNamespace);
  }

  get id(): string {
    return this.#edge.id();
  }

  get source(): BaseNode.Class {
    return new BaseNode.Class(this.#graph, this.#edge.source());
  }

  set source(node: BaseNode.Class) {
    this.#edge.move({ source: node.id });
  }

  get target(): BaseNode.Class {
    return new BaseNode.Class(this.#graph, this.#edge.target());
  }

  set target(node: BaseNode.Class) {
    this.#edge.move({ target: node.id });
  }

  is<D2 extends Data, S2 extends ScratchData>(
    guard: EdgeTypeGuard<D2, S2>,
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

  as<E extends Class<D, S>>(EdgeType: EdgeConstructor<D, S, E>): E {
    return new EdgeType(this.#graph, this.#edge, this.data, this.scratchData);
  }

  init<D2 extends Data, S2 extends ScratchData>(
    builder: EdgeBuilder<D2, S2>,
  ): Class<D2, S2> {
    const initedData = builder.buildData(this.data);
    const initedScratchData = builder.buildScratchData(this.scratchData);
    this.#edge.data(initedData);
    this.#edge.scratch(Graph.scratchNamespace, initedScratchData);
    return new Class(
      this.#graph,
      this.#edge,
      initedData,
      initedScratchData,
    );
  }

  remove() {
    this.#edge.remove();
  }

  get graph(): BaseGraph.Class {
    return this.#graph;
  }

  toCy(): cytoscape.EdgeSingular {
    return this.#edge;
  }
}

export class Builder implements EdgeBuilder<Data, ScratchData> {
  buildData(data: Data): Data {
    return data;
  }

  buildScratchData(scratchData: ScratchData): ScratchData {
    return scratchData;
  }
}

export const TypeGuard: EdgeTypeGuard<Data, ScratchData> = {
  isDataCompatible(_data: Data): _data is Data {
    return true;
  },

  isScratchDataCompatible(
    _scratchData: ScratchData,
  ): _scratchData is ScratchData {
    return true;
  },
};

export interface Data {
  id: string;
  source: string;
  target: string;
}

export interface ScratchData {}

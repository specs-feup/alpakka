
/**
 * Copyright 2024 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.alpakka.weaver;

import org.lara.interpreter.weaver.utils.LaraResourceProvider;

/**
 * This file has been automatically generated.
 * 
 * @author Joao Bispo, Luis Sousa
 *
 */
public enum AlpakkaApiJsResource implements LaraResourceProvider {

    JOINPOINTS_JS("Joinpoints.js"),
    DEFAULTDOTFORMATTER_JS("alpakka/graphs/cfg/dot/DefaultDotFormatter.js"),
    DEFAULTFLOWGRAPHDOTFORMATTER_JS("alpakka/graphs/cfg/dot/DefaultFlowGraphDotFormatter.js"),
    DOTFORMATTER_JS("alpakka/graphs/cfg/dot/DotFormatter.js"),
    FLOWGRAPH_JS("alpakka/graphs/cfg/flow/FlowGraph.js"),
    FLOWGRAPHGENERATOR_JS("alpakka/graphs/cfg/flow/FlowGraphGenerator.js"),
    CONTROLFLOWEDGE_JS("alpakka/graphs/cfg/flow/edge/ControlFlowEdge.js"),
    DATAFLOWEDGE_JS("alpakka/graphs/cfg/flow/edge/DataFlowEdge.js"),
    FLOWEDGE_JS("alpakka/graphs/cfg/flow/edge/FlowEdge.js"),
    FLOWNODE_JS("alpakka/graphs/cfg/flow/node/FlowNode.js"),
    CASENODE_JS("alpakka/graphs/cfg/flow/node/condition/CaseNode.js"),
    CONDITIONNODE_JS("alpakka/graphs/cfg/flow/node/condition/ConditionNode.js"),
    IFCOMPARISONNODE_JS("alpakka/graphs/cfg/flow/node/condition/IfComparisonNode.js"),
    TRYCATCHNODE_JS("alpakka/graphs/cfg/flow/node/condition/TryCatchNode.js"),
    FUNCTIONENTRYNODE_JS("alpakka/graphs/cfg/flow/node/instruction/FunctionEntryNode.js"),
    FUNCTIONEXITNODE_JS("alpakka/graphs/cfg/flow/node/instruction/FunctionExitNode.js"),
    GOTONODE_JS("alpakka/graphs/cfg/flow/node/instruction/GotoNode.js"),
    INSTRUCTIONNODE_JS("alpakka/graphs/cfg/flow/node/instruction/InstructionNode.js"),
    LABELNODE_JS("alpakka/graphs/cfg/flow/node/instruction/LabelNode.js"),
    RETURNNODE_JS("alpakka/graphs/cfg/flow/node/instruction/ReturnNode.js"),
    STATEMENTNODE_JS("alpakka/graphs/cfg/flow/node/instruction/StatementNode.js"),
    SWITCHNODE_JS("alpakka/graphs/cfg/flow/node/instruction/SwitchNode.js"),
    THROWNODE_JS("alpakka/graphs/cfg/flow/node/instruction/ThrowNode.js"),
    UNKNOWNINSTRUCTIONNODE_JS("alpakka/graphs/cfg/flow/node/instruction/UnknownInstructionNode.js"),
    FILTERFLOWNODES_JS("alpakka/graphs/cfg/flow/transformation/FilterFlowNodes.js"),
    BASEEDGE_JS("alpakka/graphs/cfg/graph/BaseEdge.js"),
    BASEGRAPH_JS("alpakka/graphs/cfg/graph/BaseGraph.js"),
    BASENODE_JS("alpakka/graphs/cfg/graph/BaseNode.js"),
    EDGE_JS("alpakka/graphs/cfg/graph/Edge.js"),
    GRAPH_JS("alpakka/graphs/cfg/graph/Graph.js"),
    NODE_JS("alpakka/graphs/cfg/graph/Node.js"),
    CORE_JS("core.js");

    private final String resource;

    private static final String WEAVER_PACKAGE = "alpakka/";

    /**
     * @param resource
     */
    private AlpakkaApiJsResource (String resource) {
      this.resource = WEAVER_PACKAGE + getSeparatorChar() + resource;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.SharedLibrary.Interfaces.ResourceProvider#getResource()
     */
    @Override
    public String getOriginalResource() {
        return resource;
    }

}

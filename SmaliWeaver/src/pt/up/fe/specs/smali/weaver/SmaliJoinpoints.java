/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.smali.weaver;

import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.ASmaliWeaverJoinPoint;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.smali.weaver.joinpoints.PlaceholderJp;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

public class SmaliJoinpoints {

    private static final FunctionClassMap<SmaliNode, ASmaliWeaverJoinPoint> JOINPOINT_FACTORY;
    static {
        JOINPOINT_FACTORY = new FunctionClassMap<>();
        JOINPOINT_FACTORY.put(Placeholder.class, PlaceholderJp::new);
        // JOINPOINT_FACTORY.put(Placeholder.class, node -> new PlaceholderJp(node));
    }

    public static ASmaliWeaverJoinPoint createFromLara(Object node) {
        if (!(node instanceof SmaliNode)) {
            throw new RuntimeException(
                    "Expected input to be a ClavaNode, is " + node.getClass().getSimpleName() + ": " + node);
        }

        return create((SmaliNode) node);
    }

    public static ASmaliWeaverJoinPoint create(SmaliNode node) {
        if (node == null) {
            SpecsLogs.debug("CxxJoinpoints: tried to create join point from null node, returning undefined");
            return null;
        }

        return JOINPOINT_FACTORY.apply(node);
    }

    public static <T extends AJoinPoint> T create(SmaliNode node, Class<T> targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("Check if you meant to call 'create' with a single argument");
        }

        return targetClass.cast(create(node));
    }

}

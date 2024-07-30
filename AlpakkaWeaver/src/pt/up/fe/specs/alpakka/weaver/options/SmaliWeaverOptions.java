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

package pt.up.fe.specs.alpakka.weaver.options;

import org.lara.interpreter.weaver.options.OptionArguments;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.interpreter.weaver.options.WeaverOptionBuilder;
import org.suikasoft.jOptions.Datakey.DataKey;

import java.util.HashMap;
import java.util.Map;

public class SmaliWeaverOptions {

    private static final Map<String, WeaverOption> WEAVER_OPTIONS;

    static {
        WEAVER_OPTIONS = new HashMap<>();
        WEAVER_OPTIONS.put(SmaliWeaverOption.TARGET_SDK.getName(),
                WeaverOptionBuilder.build(SmaliWeaverOption.TARGET_SDK));

        WEAVER_OPTIONS.put(SmaliWeaverOption.PACKAGE_FILTER.getName(),
                WeaverOptionBuilder.build("f", "filter", OptionArguments.ONE_ARG,
                        "Filter string", "Filter smali files to be processed by package name",
                        SmaliWeaverOption.PACKAGE_FILTER));
    }

    public static WeaverOption getOption(DataKey<?> key) {
        WeaverOption option = WEAVER_OPTIONS.get(key.getName());
        if (option != null) {
            return option;
        }

        System.out.println("Key '" + key + "' is not defined in class 'WEAVER_OPTIONS'");

        return WeaverOptionBuilder.build(key);
    }

}

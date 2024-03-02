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

package pt.up.fe.specs.smali.parser.antlr;

import java.io.File;
import java.util.List;
import java.util.Optional;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.context.SmaliContext;

public class SmaliParser {

    public Optional<SmaliNode> parse(List<File> sources) {
        var context = new SmaliContext();

        var classes = sources.stream()
                .map(file -> new SmaliFileParser(file, context).parse())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return Optional.of(classes.get(0));
    }
}

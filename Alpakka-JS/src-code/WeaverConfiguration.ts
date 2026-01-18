import WeaverConfiguration from "@specs-feup/lara/code/WeaverConfiguration.js";
import path from "path";
import { fileURLToPath } from "url";

export const weaverConfig: WeaverConfiguration = {
  weaverName: "alpakka",
  weaverPrettyName: "Alpakka",
  weaverFileName: "@specs-feup/lara/code/Weaver.js",
  jarPath: path.join(
    path.dirname(path.dirname(fileURLToPath(import.meta.url))),
    "./java-binaries/",
  ),
  javaWeaverQualifiedName: "pt.up.fe.specs.alpakka.weaver.SmaliWeaver",
  importForSideEffects: ["@specs-feup/alpakka/api/Joinpoints.js"],
};

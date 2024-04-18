import WeaverConfiguration from "lara-js/code/WeaverConfiguration.js";
import path from "path";
import { fileURLToPath } from "url";

export const weaverConfig: WeaverConfiguration = {
  weaverName: "smali",
  weaverPrettyName: "Smali",
  weaverFileName: "Weaver.js",
  jarPath: path.join(
    path.dirname(path.dirname(fileURLToPath(import.meta.url))),
    "./java-binaries/",
  ),
  javaWeaverQualifiedName: "pt.up.fe.specs.smali.weaver.SmaliWeaver",
};

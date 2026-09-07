import config from "@specs-feup/lara/oxlint.config.ts";
import { defineConfig } from "oxlint";

export default defineConfig({
  extends: [config],
  overrides: [
    {
      // Generated file: the weaver has no default attributes, so the generated
      // DefaultAttributeMap is an empty type literal.
      files: ["api/Joinpoints.ts"],
      rules: { "typescript/no-empty-object-type": "off" },
    },
    {
      // Legacy graph API: intentional marker interfaces ('Data'/'ScratchData'),
      // 'any' casts used as typechecks, and cyclic imports that are inherent to
      // the graph node/edge/graph class design. 'no-redeclare' fires on the
      // const + type alias pattern used for erasable enums (same pattern as
      // lara's InsertPosition).
      files: ["api/alpakka/graphs/**"],
      rules: {
        "typescript/no-empty-object-type": "off",
        "typescript/no-explicit-any": "off",
        "import/no-cycle": "off",
        "eslint/no-redeclare": "off",
      },
    },
  ],
});

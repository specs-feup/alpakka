import JavaTypes, {
  type JavaClasses,
} from "@specs-feup/lara/api/lara/util/JavaTypes.ts";

// oxlint-disable-next-line typescript/no-namespace
export namespace AlpakkaJavaClasses {
  /* oxlint-disable @typescript-eslint/no-empty-object-type */
  export interface AlpakkaOptions extends JavaClasses.JavaClass {}
  export interface AlpakkaOption extends JavaClasses.JavaClass {}
  /* oxlint-enable @typescript-eslint/no-empty-object-type */
}

/**
 * Static variables with class names of Java classes used in the Alpakka API.
 *
 */
export default class AlpakkaJavaTypes {
  static get AlpakkaOptions() {
    return JavaTypes.getType(
      "pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOptions"
    ) as AlpakkaJavaClasses.AlpakkaOptions;
  }

  static get AlpakkaOption() {
    return JavaTypes.getType(
      "pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOption"
    ) as AlpakkaJavaClasses.AlpakkaOption;
  }
}

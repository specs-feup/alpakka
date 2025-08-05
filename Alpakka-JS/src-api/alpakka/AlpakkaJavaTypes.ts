import JavaTypes, {
  JavaClasses,
} from "@specs-feup/lara/api/lara/util/JavaTypes.js";

// eslint-disable-next-line @typescript-eslint/no-namespace
export namespace AlpakkaJavaClasses {
  /* eslint-disable @typescript-eslint/no-empty-object-type */
  export interface AlpakkaOptions extends JavaClasses.JavaClass {}
  /* eslint-enable @typescript-eslint/no-empty-object-type */
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
}

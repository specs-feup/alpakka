import JavaTypes from "@specs-feup/lara/api/lara/util/JavaTypes.js";
import { wrapJoinPoint } from "@specs-feup/lara/api/LaraJoinPoint.js";
import { Statement } from "../Joinpoints.js";

/**
 * Factory for creating Alpakka AST constructs.
 * Use the standard `insertBefore`/`insertAfter` actions on existing statements to place them in the AST.
 */
export default class AlpakkaFactory {
  private static get _java() {
    return JavaTypes.getType("pt.up.fe.specs.alpakka.weaver.AlpakkaFactory");
  }

  /**
   * Creates the flat list of statements that make up a packed-switch construct —
   * the same sequence the parser would produce as direct children of a method body:
   * `[switch-instruction, :case_0, :case_1, ..., :data-label, .packed-switch-directive]`.
   *
   * Insert each statement with `existingStmt.insertBefore(stmt)` (in order),
   * then populate case bodies via `caseLabel.insertAfter(...)`.
   *
   * @param register  register to switch on (e.g. `"v0"`)
   * @param numCases  number of cases
   */
  static packedSwitch(register: string, numCases: number): Statement[] {
    return wrapJoinPoint(
      AlpakkaFactory._java.packedSwitch(register, numCases)
    ) as Statement[];
  }
}

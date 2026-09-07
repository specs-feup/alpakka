import { AlpakkaLegacyTester } from "../vitest/AlpakkaLegacyTester.ts";
import path from "path";

/* oxlint-disable vitest/expect-expect */
describe("Legacy Integration Tests", () => {
    function newTester() {
        return new AlpakkaLegacyTester(
            path.resolve("../AlpakkaWeaver/test-resources/alpakka/test/weaver")
        )
            .setResultPackage("results")
            .setSrcPackage("src");
    }

    it("Basic", async () => {
        await newTester().test("HelloWorld.js", "HelloWorld.smali");
    }, 10_000);

    it("Manifest", async () => {
        await newTester().test("Manifest.js", "Bankdroid-rev-2b0345b5c2.apk");
    }, 30_000);
});

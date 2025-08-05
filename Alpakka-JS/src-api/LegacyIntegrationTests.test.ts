import { AlpakkaLegacyTester } from "../jest/AlpakkaLegacyTester.js";
import path from "path";

/* eslint-disable jest/expect-expect */
describe("Legacy Integration Tests", () => {
    function newTester() {
        return new AlpakkaLegacyTester(
            path.resolve("../AlpakkaWeaver/test-resources")
        )
            .setResultPackage("results")
            .setSrcPackage("src");
    }

    it("Basic", async () => {
        await newTester().test("HelloWorld.js", "HelloWorld.smali");
    });

    it("Manifest", async () => {
        await newTester().test("Manifest.js", "Bankdroid-rev-2b0345b5c2.apk");
    });
});

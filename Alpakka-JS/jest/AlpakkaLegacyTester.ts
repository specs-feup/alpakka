import { WeaverLegacyTester } from "@specs-feup/lara/jest/WeaverLegacyTester.js";
import AlpakkaJavaTypes from "@specs-feup/alpakka/api/alpakka/AlpakkaJavaTypes.js";

export class AlpakkaLegacyTester extends WeaverLegacyTester {
    public constructor(basePackage: string) {
        super(basePackage);
        this.set(AlpakkaJavaTypes.AlpakkaOption.TARGET_SDK, 20);
    }
}

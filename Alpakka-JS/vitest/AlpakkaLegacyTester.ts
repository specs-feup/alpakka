import { WeaverLegacyTester } from "@specs-feup/lara/vitest/WeaverLegacyTester.ts";
import AlpakkaJavaTypes from "@specs-feup/alpakka/api/alpakka/AlpakkaJavaTypes.ts";

export class AlpakkaLegacyTester extends WeaverLegacyTester {
    public constructor(basePackage: string) {
        super(basePackage);
        this.set(AlpakkaJavaTypes.AlpakkaOption.TARGET_SDK, 20);
    }
}

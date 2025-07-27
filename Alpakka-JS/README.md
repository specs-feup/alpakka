# Alpakka
Alpakka is a [LARA Framework](https://github.com/specs-feup/lara-framework) source-to-source compiler for Android's [smali](https://github.com/google/smali) syntax. It enables the analysis and transformation of Android apps through APK files.

The name "Alpakka" derives from the Icelandic word for package, "Pakka", reflecting its core functionality in handling Android Packages (APKs).


## Local Development

To locally develop Alpakka, follow these steps:

1. Clone the repositories for Alpakka, the [LARA Framework](https://github.com/specs-feup/lara-framework) and [Specs Java Libs](https://github.com/specs-feup/specs-java-libs) to the root of your workspace.

2. You'll need to create a `package.json` file at the root of your workspace with the following contents:

```json
{
  "type": "module",
  "workspaces": [
    "lara-framework/Lara-JS",
    "alpakka/Alpakka-JS",
  ]
}
```

3. Run the following commands at the root of your workspace to install dependencies and build the projects:

```bash
npm install
cd lara-framework/Lara-JS
npm run build
cd ../../alpakka/AlpakkaWeaver
gradle installDist
mv build/install/AlpakkaWeaver/lib/* ../Alpakka-JS/java-binaries/
cd ../../alpakka/Alpakka-JS
npm install
npm run build
```

## Running Alpakka

To execute Alpakka, use the following command:

```bash
npx alpakka classic <scriptToRun.js> -p <input file>
```

Replace `<scriptToRun.js>` with your JavaScript file and `<input file>` with the input APK or smali files you want to analyze.

Alpakka-JS comes with a built-in script to detect and correct resource leaks in Android apps. You can try it by specifying `./scripts/DetectAndCorrectLeaks.js` as the script to run.

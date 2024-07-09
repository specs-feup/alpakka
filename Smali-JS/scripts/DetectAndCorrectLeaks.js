import Query from "lara-js/api/weaver/Query.js";
import WeaverOptions from "lara-js/api/weaver/WeaverOptions.js";
import FlowGraph from "smali-js/api/smali/graphs/cfg/flow/FlowGraph.js";
// import DefaultFlowGraphDotFormatter from "smali-js/api/smali/graphs/cfg/dot/DefaultFlowGraphDotFormatter.js";
import LeaksDetection from "./LeaksDetection.js";
import LeaksCorrection from "./LeaksCorrection.js";
import fs from "fs";
import path from "path";

const workspace = WeaverOptions.toCli(); // getData().getSourceFolders();
const apkName = workspace.split(path.sep).pop().slice(0, -4);

const app = Query.root();

console.log("\nGenerating flow graph...\n");

let timeInMs = Date.now();

const graph = FlowGraph.generate(app);

// graph.toDotFile(new DefaultFlowGraphDotFormatter(), "output.dot");

const cfgTime = Date.now() - timeInMs;

console.log("\nFlow graph generated.\n");

const manifest = app.manifest;

const detector = new LeaksDetection(app, graph);

let leaksResult = {};
let fieldsResult = {};

const componentMethods = [
  "onConfigurationChanged(Landroid/content/res/Configuration;)V",
];

const activities = manifest.activities;

const activityMethods = [
  "<init>()V",
  "onCreate(Landroid/os/Bundle;)V",
  "onStart()V",
  "onResume()V",
  "onPause()V",
  "onStop()V",
  "onDestroy()V",
];

console.log("\nDetecting resource leaks...");

timeInMs = Date.now();

for (const activity of activities) {
  const [leaksMap, fieldsMap] = detector.findResourceLeaksInClass(
    activity,
    activityMethods,
  );

  saveUniqueLeaks(leaksResult, fieldsResult, leaksMap, fieldsMap);
}

const services = manifest.services;

const serviceMethods = [
  "<init>()V",
  "onCreate()V",
  "onStartCommand(Landroid/content/Intent;II)I",
  "onBind(Landroid/content/Intent;)Landroid/os/IBinder;",
  "onUnbind(Landroid/content/Intent;)Z",
  "onDestroy()V",
];

for (const service of services) {
  const [leaksMap, fieldsMap] = detector.findResourceLeaksInClass(
    service,
    serviceMethods,
  );

  saveUniqueLeaks(leaksResult, fieldsResult, leaksMap, fieldsMap);
}

const detectionTime = Date.now() - timeInMs;

let leaksOutput = "";

for (const leak in leaksResult) {
  const lineInfo = getLineInfo(leaksResult[leak].jpList[0]);
  const leakMessage =
    leaksResult[leak].resource.type +
    " leak in " +
    leaksResult[leak].jpList[0].parent.referenceName +
  "\n";
    // + "Acquisition Jp: " +
    // leaksResult[leak].acquisitionJp.code +
    // "\n" +
    // "Register: " +
    // leak +
    // "\n" +
    // "Code: " +
    // leaksResult[leak].jpList[0].code +
    // "\n";

  console.log("\n\n" + lineInfo + "Found " + leakMessage);

  leaksOutput += lineInfo + leakMessage + "\n\n";
}

// const packageName = manifest.packageName;

// console.log("Leaks: ", leaksMap);

// console.log("Fields: ", fieldsMap);

const refactor = new LeaksCorrection(manifest, graph);

let correctionTime = -1;
let rebuildTime = -1;

if (Object.keys(leaksResult).length > 0) {
  console.log("\nCorrecting resource leaks...\n");

  timeInMs = Date.now();

  refactor.releaseLeaks(leaksResult, fieldsResult);

  correctionTime = Date.now() - timeInMs;
  timeInMs = Date.now();

  try {
    app.buildApk("/results/" + apkName + "-alpakkaFix" + ".apk");
  } catch (e) {
    console.log("Error rebuilding APK: ", e);
  }

  rebuildTime = Date.now() - timeInMs;
} else {
  console.log("\n\nNo resource leaks found.\n");
}

leaksOutput += "\n\n" + "CFG Generation Time: " + cfgTime + "ms\n";
leaksOutput += "\n" + "Detection Time: " + detectionTime + "ms\n";
leaksOutput += "\n" + "Correction Time: " + correctionTime + "ms\n";
leaksOutput += "\n" + "Rebuild Time: " + rebuildTime + "ms\n";
leaksOutput += "\n" + "Nodes: " + graph.nodes.length + "\n";
leaksOutput += "\n" + "Edges: " + graph.edges.length + "\n";
leaksOutput += "\n" + "Functions: " + graph.functions.length + "\n";

fs.writeFileSync("/results/" + apkName + "-leaksOutput" + ".txt", leaksOutput);

function getLineInfo(jp) {
  let line = undefined;
  let currentJp = jp;

  while (currentJp !== undefined) {
    if (currentJp.line !== undefined) {
      line = currentJp.line.value.code;

      return "Line " + line + ": ";
    }

    currentJp = currentJp.prevStatement;
  }

  return "";
}

function saveUniqueLeaks(leaksResult, fieldsResult, leaksMap, fieldsMap) {
  // TODO: It's possible two registers are holding the same resource,
  // we should save the smallest register in order to stay within the 4 bits correction limit

  for (const leak in leaksMap) {
    let isDuplicate = false;
    for (const key in leaksResult) {
      if (
        leaksResult[key].acquisitionJp.id === leaksMap[leak].acquisitionJp.id
      ) {
        if (!detector.resourceLeaksAreEqual(leaksResult[key], leaksMap[leak])) {
          console.log("Duplicate leak found: ", leaksMap[leak].jpList[0].code);
        }

        isDuplicate = true;
        break;
      }
    }

    if (isDuplicate) {
      continue;
    }

    if (leaksResult[leak] !== undefined) {
      detector.moveLeaksDataToSafeRegister(leaksResult, fieldsResult, leak);
    }

    leaksResult[leak] = leaksMap[leak];
    fieldsResult[leak] = fieldsMap[leak];
  }
}

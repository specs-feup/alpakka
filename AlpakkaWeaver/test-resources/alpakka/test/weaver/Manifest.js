import Query from "@specs-feup/lara/api/weaver/Query.js";

const app = Query.root();

const manifest = app.manifest;

console.log("Activities:");
for (const activity of manifest.activities) {
    console.log(activity);
}
console.log();

console.log("Services:");
for (const service of manifest.services) {
    console.log(service);
}
console.log();

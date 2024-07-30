laraImport("weaver.Query");

const app = Query.root();

const manifest = app.manifest;

console.log("Activities:\n");
for (const activity of manifest.activities) {
    console.log(activity + "\n");
}

console.log("Services:\n");
for (const service of manifest.services) {
    console.log(service + "\n");
}


import FunctionExitNode from "smali-js/api/smali/graphs/cfg/flow/node/instruction/FunctionExitNode.js";

const releaseMethods = [];

class LeaksCorrection {
  constructor(manifest, graph) {
    this.manifest = manifest;
    this.graph = graph;
  }

  releaseLeaks(leaksObject, fieldsObject) {
    const activityMethods = [
      "<init>",
      "onCreate",
      "onStart",
      "onResume",
      "onPause",
      "onStop",
      "onDestroy",
    ];

    for (const leak in leaksObject) {
      if (leaksObject[leak].resource.hasDependencies) {
        console.log(
          "\nReleasing " +
            leaksObject[leak].resource.type +
            " resources is not supported yet.\n",
        );
        continue;
      }

      const parentMethod = leaksObject[leak].jpList[0].parent;
      const parentClass = parentMethod.parent;

      const activities = this.manifest.activities;
      const services = this.manifest.services;

      const fullyQualifiedClassName = parentClass.classDescriptor.code
        .substring(1, parentClass.classDescriptor.code.length - 1)
        .replaceAll("/", ".");

      if (
        !activities.includes(fullyQualifiedClassName) &&
        !services.includes(fullyQualifiedClassName)
      ) {
        const node = this.graph.getFunction(parentMethod.referenceName);

        if (node !== undefined) {
          this.releaseResourceLeakInMethod(
            node,
            leak,
            leaksObject[leak],
            fieldsObject,
          );
        }

        continue;
      }

      if (fieldsObject[leak] !== undefined) {
        const fieldClass = fieldsObject[leak].substring(
          0,
          fieldsObject[leak].indexOf("->"),
        );

        if (fieldClass !== parentClass.classDescriptor.code) {
          delete fieldsObject[leak];
        }
      }

      const indexInLifecycle = activityMethods.indexOf(parentMethod.name);

      if (
        fieldsObject[leak] === undefined &&
        indexInLifecycle < activityMethods.indexOf("onPause")
      ) {
        if (parentMethod.isStatic) {
          console.log("\nStatic methods are not supported yet.\n");
          continue;
        }

        const register = leak.replaceAll("_", "");

        // Assumes locals are being used and not registers
        const registersDirective = parentMethod.registersDirective;
        const numberOfLocals = parseInt(registersDirective.value.code);
        const thisRegNumber = numberOfLocals;

        const regNumber = register.substring(1);

        // TODO: We can do a move operation here to solve this
        if (thisRegNumber > 15 || regNumber > 15) {
          console.log(
            "\n\nRegister has more than 4 bits and is not supported for resource releasing yet.\n\n",
          );
          continue;
        }

        const firstMethod = parentClass.methods[0];

        const fieldName = leak + ":" + leaksObject[leak].resource.type;

        firstMethod.insertBefore(".field public " + fieldName + "\n\n");

        fieldsObject[leak] =
          parentClass.classDescriptor.code + "->" + fieldName;

        for (const jp of leaksObject[leak].jpList) {
          const putFieldInstruction =
            "iput-object " +
            register +
            ", p0, " +
            parentClass.classDescriptor.code +
            "->" +
            fieldName;
          jp.insertAfter("\n" + putFieldInstruction + "\n\n");
        }
      }

      const onPause = this.graph.getFunction(
        parentClass.classDescriptor.code + "->" + "onPause()V",
      );

      if (
        onPause !== undefined &&
        indexInLifecycle >= activityMethods.indexOf("onResume") &&
        indexInLifecycle <= activityMethods.indexOf("onPause")
      ) {
        this.releaseResourceLeakInMethod(
          onPause,
          leak,
          leaksObject[leak],
          fieldsObject,
        );
        continue;
      }

      const onStop = this.graph.getFunction(
        parentClass.classDescriptor.code + "->" + "onStop()V",
      );

      if (
        onStop !== undefined &&
        indexInLifecycle >= activityMethods.indexOf("onStart") &&
        indexInLifecycle <= activityMethods.indexOf("onStop")
      ) {
        this.releaseResourceLeakInMethod(
          onStop,
          leak,
          leaksObject[leak],
          fieldsObject,
        );
        continue;
      }

      const onDestroy = this.graph.getFunction(
        parentClass.classDescriptor.code + "->" + "onDestroy()V",
      );

      if (onDestroy !== undefined) {
        this.releaseResourceLeakInMethod(
          onDestroy,
          leak,
          leaksObject[leak],
          fieldsObject,
        );
        continue;
      }

      this.overrideOnDestroyToReleaseResource(
        parentClass,
        leaksObject[leak],
        leak,
        fieldsObject,
      );
    }
  }

  overrideOnDestroyToReleaseResource(
    parentClass,
    resourceLeak,
    register,
    fieldsObject,
  ) {
    const lastMethod = parentClass.methods[parentClass.methods.length - 1];

    let onDestroy = ".method public onDestroy()V\n" + "\t.locals 1\n\n";

    if (
      parentClass.superClassDescriptor !== undefined &&
      parentClass.superClassDescriptor.code !== ""
    ) {
      onDestroy +=
        "\tinvoke-super/range {p0 .. p0}, " +
        parentClass.superClassDescriptor.code +
        "->onDestroy()V\n\n";
    }

    onDestroy += this.getFieldInstructionString(
      "v0",
      parentClass,
      register,
      fieldsObject,
    );

    onDestroy += this.releaseInstructionString(
      "v0",
      parentClass,
      resourceLeak.resource,
    );

    onDestroy += "\treturn-void\n" + ".end method\n\n";

    const releaseMethod = this.releaseMethodString(resourceLeak.resource);

    lastMethod.insertAfter(onDestroy + "\n" + releaseMethod);

    console.log(
      "\n\nOverriding " +
        parentClass.classDescriptor.code +
        "->onDestroy()V" +
        " to release " +
        resourceLeak.resource.type +
        "\n\n",
    );

    return;
  }

  releaseResourceLeakInMethod(methodNode, register, leak, fieldsObject) {
    const parentMethod = leak.jpList[0].parent;
    const parentClass = parentMethod.parent;
    const cleanRegister = register.replaceAll("_", "");

    const resourceType = leak.resource.type.substring(
      leak.resource.type.lastIndexOf("/") + 1,
      leak.resource.type.length - 1,
    );

    if (
      !releaseMethods.includes(
        parentClass.classDescriptor.code + "->" + "tryClose" + resourceType,
      )
    ) {
      parentMethod.insertAfter(this.releaseMethodString(leak.resource));
      releaseMethods.push(
        parentClass.classDescriptor.code + "->" + "tryClose" + resourceType,
      );
    }

    if (parentMethod.referenceName === methodNode.jp.referenceName) {
      const releaseInstruction = this.releaseInstructionString(
        cleanRegister,
        parentClass,
        leak.resource,
      );
      for (const jp of leak.jpList) {
        jp.insertAfter("\n" + releaseInstruction);
      }

      console.log(
        "\n\nAttempted to release " +
          resourceType +
          " in " +
          methodNode.jp.referenceName +
          "\n\n",
      );

      return;
    }

    // Assumes smali files are using the parameters naming scheme, which Apktool does use
    const registersDirective = methodNode.jp.registersDirective;

    const numberOfRegisters = parseInt(registersDirective.value.code);

    registersDirective.value.setValue((numberOfRegisters + 1).toString());

    let freeRegister = "v";
    if (registersDirective.type === "I_REGISTERS") {
      let registersMinusParameters =
        numberOfRegisters - methodNode.jp.prototype.parameters.length;
      if (!methodNode.jp.isStatic) {
        registersMinusParameters--;
      }
      freeRegister += registersMinusParameters;
    } else {
      freeRegister += numberOfRegisters;
    }

    let functionExitNode = methodNode.nextNodes[0];

    while (
      functionExitNode.nextNodes.length > 0 &&
      !functionExitNode.is(FunctionExitNode.TypeGuard)
    ) {
      functionExitNode = functionExitNode.nextNodes[0];
    }

    for (const incomingEdge of functionExitNode.incomers) {
      const sourceNode = this.graph.getNode(incomingEdge.source.id);

      const getFieldInstruction = this.getFieldInstructionString(
        freeRegister,
        parentClass,
        register,
        fieldsObject,
      );

      const releaseResourceInstruction = this.releaseInstructionString(
        freeRegister,
        parentClass,
        leak.resource,
      );

      // console.log(getFieldInstruction + releaseResourceInstruction);

      sourceNode.jp.insertBefore(
        getFieldInstruction + releaseResourceInstruction,
      );
    }

    console.log(
      "\n\nAttempted to release " +
        resourceType +
        " in " +
        methodNode.jp.referenceName +
        "\n\n",
    );

    return;
  }

  releaseMethodString(resource) {
    let resourceType = resource.type.substring(
      resource.type.lastIndexOf("/") + 1,
      resource.type.length - 1,
    );

    let releaseMethod =
      "\n.method public static tryClose" +
      resourceType +
      "(" +
      resource.type +
      ")V\n" +
      "\t.locals 1\n\n" +
      "\tif-eqz p0, :cond_0\n\n";

    if (resource.isReleasedMethod !== "") {
      if (resource.isReleasedMethod.startsWith("!")) {
        const isReleasedMethod = resource.isReleasedMethod.substring(1);
        releaseMethod +=
          "\tinvoke-interface/range {p0 .. p0}, " +
          resource.type +
          "->" +
          isReleasedMethod +
          "()Z\n\n" +
          "\tmove-result v0\n\n" +
          "\tif-eqz v0, :cond_0\n\n";
      } else {
        releaseMethod +=
          "\tinvoke-interface/range {p0 .. p0}, " +
          resource.type +
          "->" +
          resource.isReleasedMethod +
          "()Z\n\n" +
          "\tmove-result v0\n\n" +
          "\tif-nez v0, :cond_0\n\n";
      }
    }

    releaseMethod +=
      "\tinvoke-interface/range {p0 .. p0}, " +
      resource.type +
      "->" +
      resource.releaseMethod[0] +
      "()V\n\n" +
      "\t:cond_0\n" +
      "\treturn-void\n" +
      ".end method\n\n";

    return releaseMethod;
  }

  releaseInstructionString(registerToRelease, parentClass, resource) {
    const resourceType = resource.type.substring(
      resource.type.lastIndexOf("/") + 1,
      resource.type.length - 1,
    );

    const releaseInstruction =
      "\n\tinvoke-static/range {" +
      registerToRelease +
      " .. " +
      registerToRelease +
      "}, " +
      parentClass.classDescriptor.code +
      "->tryClose" +
      resourceType +
      "(" +
      resource.type +
      ")V\n\n";

    return releaseInstruction;
  }

  getFieldInstructionString(
    freeRegister,
    parentClass,
    fieldRegister,
    fieldsObject,
  ) {
    let getFieldInstruction;

    let fieldIsStatic = false;

    for (const field of parentClass.fields) {
      if (field.referenceName === fieldsObject[fieldRegister]) {
        fieldIsStatic = field.isStatic;
        break;
      }
    }

    if (fieldIsStatic) {
      getFieldInstruction = "\n\tsget-object " + freeRegister;
    } else {
      getFieldInstruction = "\n\tiget-object " + freeRegister + ", p0";
    }

    getFieldInstruction += ", " + fieldsObject[fieldRegister] + "\n\n";

    return getFieldInstruction;
  }

  getNextInstruction(statement) {
    let nextInstruction = statement.nextStatement;
    while (
      nextInstruction !== undefined &&
      !(nextInstruction instanceof Instruction)
    ) {
      nextInstruction = nextInstruction.nextStatement;
    }

    return nextInstruction;
  }
}

export default LeaksCorrection;

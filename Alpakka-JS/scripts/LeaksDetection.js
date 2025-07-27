import {
  IfComparisonWithZero,
  Instruction,
  Joinpoint,
  Label,
  MethodReference,
  RegisterList,
  RegisterRange,
  RegisterReference,
  ReturnStatement,
  Statement,
} from "@specs-feup/alpakka/api/Joinpoints.js";
import FunctionExitNode from "../api/alpakka/graphs/cfg/flow/node/instruction/FunctionExitNode.js";
import Resources from "./Resources.js";
import ResourceLeak from "./ResourceLeak.js";
import ConditionNode from "../api/alpakka/graphs/cfg/flow/node/condition/ConditionNode.js";
import TryCatchNode from "../api/alpakka/graphs/cfg/flow/node/condition/TryCatchNode.js";

const resources = new Resources().getAllResources();

let methodCallResult = undefined;

class LeaksDetection {
  constructor(programJp, graph) {
    this.app = programJp;
    this.graph = graph;
  }

  findResourceLeaksInClass(fullyQualifiedClassName, methodsToSearch) {
    const className = "L" + fullyQualifiedClassName.replaceAll(".", "/") + ";";

    // console.log("\n\nClass: ", className);

    // These aren't actually maps, but they probably should be
    let leaksMap = {};
    let fieldsMap = {};

    let visitedMethods = {};

    for (const method of methodsToSearch) {
      let node = undefined;

      let classNode = this.app.classes.find(
        (classJp) => classJp.classDescriptor.code === className,
      );

      while (classNode !== undefined) {
        node = this.graph.getFunction(
          classNode.classDescriptor.code + "->" + method,
        );
        if (node !== undefined) {
          break;
        }
        classNode = this.app.classes.find(
          (classJp) =>
            classJp.classDescriptor.code ===
            classNode.superClassDescriptor.code,
        );
      }

      if (node === undefined) {
        continue;
      }

      if (!node.jp.isStatic) {
        fieldsMap["p0"] = node.jp.parent.classDescriptor.code;
      } else if (
        leaksMap["p0"] !== undefined ||
        fieldsMap["p0"] !== undefined
      ) {
        this.moveLeaksDataToSafeRegister(leaksMap, fieldsMap, "p0");
      }

      // console.log("\n\nMethod: ", method);

      leaksMap = this.findResourceLeaksInFunction(
        node,
        leaksMap,
        fieldsMap,
        visitedMethods,
      );

      // console.log("\n\nLeaks: ", leaksMap);
      // console.log("\n\nFields: ", fieldsMap);

      const keys = Object.keys(fieldsMap).concat(Object.keys(leaksMap));
      for (const key of keys) {
        if (key === "p0") {
          continue;
        }

        this.moveLeaksDataToSafeRegister(leaksMap, fieldsMap, key);
      }
    }

    return [leaksMap, fieldsMap];

    // console.log(fieldsMap);
  }

  findResourceLeaksInFunction(
    node,
    leaks = {},
    fields = {},
    visitedMethods = {},
  ) {
    let nodesToVisit = [];
    let visitedNodes = new Set();
    nodesToVisit.push({
      currNode: node,
      currLeaks: leaks,
      currFields: fields,
      currIsClosedOps: {},
      functionVisitedNodes: visitedNodes,
    });

    while (nodesToVisit.length > 0) {
      let {
        currNode,
        currLeaks,
        currFields,
        currIsClosedOps,
        functionVisitedNodes,
      } = nodesToVisit.pop();

      let existingVisitedObj = undefined;

      for (const visitedObject of functionVisitedNodes) {
        if (
          this.joinpointsAreEqual(currNode.jp, visitedObject.node.jp) &&
          currNode.id === visitedObject.node.id
        ) {
          existingVisitedObj = visitedObject;
          break;
        }
      }

      if (existingVisitedObj !== undefined) {
        if (this.leaksObjectsAreEqual(currLeaks, existingVisitedObj.leaks)) {
          continue;
        }

        let leaksObjects = [];

        for (const incomingEdge of currNode.incomers) {
          const sourceNode = this.graph.getNode(incomingEdge.source.id);
          for (const object of functionVisitedNodes) {
            if (this.joinpointsAreEqual(object.node.jp, sourceNode.jp)) {
              let sourceIsClosedOps = this.deepClone(object.isClosedOps);

              const visitedLeaks = this.processComparison(
                sourceNode,
                currNode,
                this.deepClone(object.leaks),
                sourceIsClosedOps,
              );

              if (Object.keys(visitedLeaks).length > 0) {
                leaksObjects.push(visitedLeaks);

                currFields = this.deepClone(object.fields);
                currIsClosedOps = sourceIsClosedOps;
              }

              break;
            }
          }
        }

        if (leaksObjects.length > 0) {
          currLeaks = this.combineLeaksObjects(leaksObjects, currNode.jp);
        }
      }

      if (currNode.is(FunctionExitNode.TypeGuard)) {
        this.processFunctionExit(currNode, currLeaks, currFields, nodesToVisit);
      }

      if (currNode.jp instanceof Instruction) {
        // console.log("\nVisiting node: ", currNode.jp.code);

        const nodesToVisitLengthBefore = nodesToVisit.length;

        // if (currNode.jp.parent.referenceName === "Linfo/guardianproject/otr/app/im/provider/Imps$ProviderSettings;->getSettingValue(Landroid/content/ContentResolver;JLjava/lang/String;)Landroid/database/Cursor;") {
        //   console.log("\nVisiting node: ", currNode.jp.code);
        // }

        // we could have a state object so we don't have to pass all these arguments
        this.processInstruction(
          currNode,
          currLeaks,
          currFields,
          currIsClosedOps,
          visitedMethods,
          nodesToVisit,
          functionVisitedNodes,
        );

        // if (currNode.jp.parent.referenceName === "Linfo/guardianproject/otr/app/im/provider/Imps$ProviderSettings;->getSettingValue(Landroid/content/ContentResolver;JLjava/lang/String;)Landroid/database/Cursor;") {
        //   // console.log("\nFields: ", currFields);
        //   console.log("\nLeaks: ", currLeaks);
        // }

        if (nodesToVisit.length > nodesToVisitLengthBefore) {
          continue;
        }

        // console.log("\nLeaks: ", currLeaks);
      }

      if (existingVisitedObj !== undefined) {
        if (this.leaksObjectsAreEqual(existingVisitedObj.leaks, currLeaks)) {
          continue;
        }

        functionVisitedNodes.delete(existingVisitedObj);
      }

      functionVisitedNodes.add({
        node: currNode,
        leaks: currLeaks,
        fields: currFields,
        isClosedOps: currIsClosedOps,
      });

      for (let i = 0; i < currNode.nextNodes.length; i++) {
        let newIsClosedOps = this.deepClone(currIsClosedOps);

        const nodeLeaks = this.processComparison(
          currNode,
          currNode.nextNodes[i],
          this.deepClone(currLeaks),
          newIsClosedOps,
        );

        nodesToVisit = nodesToVisit.filter(
          (node) =>
            !this.joinpointsAreEqual(
              node.currNode.jp,
              currNode.nextNodes[i].jp,
            ) || !this.leaksObjectsAreEqual(node.currLeaks, nodeLeaks),
        );

        nodesToVisit.push({
          currNode: currNode.nextNodes[i],
          currLeaks: nodeLeaks,
          currFields: this.deepClone(currFields),
          currIsClosedOps: newIsClosedOps,
          functionVisitedNodes: functionVisitedNodes,
        });
      }
    }

    let lastVisitedItem = undefined;

    for (const item of visitedNodes) {
      if (
        item.node.is(FunctionExitNode.TypeGuard) &&
        item.node.jp.referenceName === node.jp.referenceName
      ) {
        lastVisitedItem = item;
        break;
      }
    }

    if (lastVisitedItem === undefined) {
      console.log("Something went wrong.");
      return {};
    }

    leaks = lastVisitedItem.leaks;

    for (const field in fields) {
      delete fields[field];
    }
    for (const field in lastVisitedItem.fields) {
      fields[field] = lastVisitedItem.fields[field];
    }

    // console.log("Returning leaks: ", leaks);

    return leaks;
  }

  processFunctionExit(node, leaks, fields, nodesToVisit) {
    if (nodesToVisit.length > 0) {
      const methodReferenceName = node.jp.referenceName;
      // console.log("Method reference name: ", methodReferenceName);

      let originalMethodCall = undefined;

      for (let i = nodesToVisit.length - 1; i >= 0; i--) {
        if (
          this.isMethodCall(nodesToVisit[i].currNode.jp) &&
          nodesToVisit[i].currNode.jp.children[1].code === methodReferenceName
        ) {
          originalMethodCall = nodesToVisit[i];
          break;
        }
      }

      if (originalMethodCall === undefined) {
        return;
      }

      let resultingLeaks = {};
      let resultingFields = {};

      const registers = this.getRegisterReferences(
        originalMethodCall.currNode.jp,
      );

      const uniqueKeys = new Set(
        Object.keys(leaks).concat(Object.keys(fields)),
      );

      for (const key of uniqueKeys) {
        let existingRegister = undefined;
        for (const leak in originalMethodCall.currLeaks) {
          if (leaks[key] === undefined) {
            break;
          }
          if (
            originalMethodCall.currLeaks[leak].acquisitionJp.id ===
            leaks[key].acquisitionJp.id
          ) {
            existingRegister = leak;
            break;
          }
        }

        if (existingRegister !== undefined) {
          this.moveLeaksDataToSafeRegister(
            resultingLeaks,
            resultingFields,
            existingRegister,
          );
          if (leaks[key] !== undefined)
            resultingLeaks[existingRegister] =
              originalMethodCall.currLeaks[existingRegister];
          if (fields[key] !== undefined)
            resultingFields[existingRegister] =
              originalMethodCall.currFields[existingRegister];
          continue;
        }

        if (key.startsWith("p") && !key.endsWith("_")) {
          const index = Number(key.slice(1));
          if (registers[index] !== undefined) {
            this.moveLeaksDataToSafeRegister(
              resultingLeaks,
              resultingFields,
              registers[index],
            );
            if (leaks[key] !== undefined)
              resultingLeaks[registers[index]] = leaks[key];
            if (fields[key] !== undefined)
              resultingFields[registers[index]] = fields[key];
          }

          continue;
        }

        const resultingKeys = new Set(
          Object.keys(resultingLeaks).concat(Object.keys(resultingFields)),
        );
        let safeRegister = key + "_";
        while (resultingKeys.has(safeRegister)) {
          safeRegister += "_";
        }

        if (leaks[key] !== undefined) resultingLeaks[safeRegister] = leaks[key];
        if (fields[key] !== undefined)
          resultingFields[safeRegister] = fields[key];

        this.moveLeaksDataToSafeRegister(
          resultingLeaks,
          resultingFields,
          safeRegister,
        );
      }

      originalMethodCall.currLeaks = resultingLeaks;
      originalMethodCall.currFields = resultingFields;
    }
  }

  processInstruction(
    node,
    leaks,
    fields,
    isClosedOps,
    visitedMethods,
    nodesToVisit,
    visitedNodes,
  ) {
    // console.log("\nProcessing instruction: ", node.jp.code, "\n");
    // console.log("Leaks: ", leaks, "\n");
    // console.log("Fields: ", fields, "\n");

    if (node.jp.setsRegister) {
      const register = node.jp.children[0];

      if (!(register instanceof RegisterReference)) {
        console.log("Register reference not found: ", node.jp.code);
      }

      if (
        (leaks[register.code] !== undefined &&
          !this.joinpointsAreEqual(
            node.jp,
            leaks[register.code].getLastJoinpoint().nextStatement,
          ) &&
          !this.joinpointsAreEqual(
            node.jp,
            leaks[register.code].getLastJoinpoint(),
          )) ||
        fields[register.code] !== undefined
      ) {
        this.moveLeaksDataToSafeRegister(leaks, fields, register.code);
      }
    }

    if (this.isMethodCall(node.jp)) {
      //   console.log("Method call: ", node.jp.code);

      this.processMethodCall(
        node,
        leaks,
        fields,
        isClosedOps,
        visitedMethods,
        nodesToVisit,
        visitedNodes,
      );
    }

    let registerList = this.getRegisterReferences(node.jp);

    for (const register of registerList) {
      if (leaks[register] === undefined || leaks[register].length === 0) {
        continue;
      }

      let replacementJp = node.jp;
      const nextInstruction = this.getNextInstruction(node.jp);

      if (
        nextInstruction !== undefined &&
        nextInstruction.opCodeName.startsWith("move-result")
      ) {
        replacementJp = nextInstruction;
      }

      leaks[register].removeJoinpoint();
      leaks[register].addJoinpoint(replacementJp);
    }

    if (this.isHeldValidation(node.jp, leaks)) {
      let nextInstruction = this.getNextInstruction(node.jp);

      if (
        nextInstruction.opCodeName !== undefined &&
        nextInstruction.opCodeName.startsWith("move-result")
      ) {
        isClosedOps[nextInstruction.children[0].code] =
          this.getRegisterReferences(node.jp)[0];
      }
    }

    if (this.isResourceAcquisition(node.jp)) {
      // console.log("Resource acquisition: ", node.jp.code);
      this.processResourceAcquisition(node, leaks, fields);
    }

    if (this.isResourceRelease(node.jp, leaks)) {
      // console.log("Resource release: ", node.jp.code);

      const rList = node.jp.children[0];
      // const register = rList.children[0].code;
      let register = undefined;

      for (const reg of rList.children) {
        if (leaks[reg.code] !== undefined) {
          register = reg.code;
          break;
        }
      }

      if (register !== undefined) {
        this.releaseResource(leaks, register);
      }
    }

    // if (node.jp.opCodeName === "new-instance") {
    //   const destination = node.jp.children[0].code;
    //   const resource = node.jp.children[1].code;

    //   fields[destination] = resource;
    // }

    this.processFieldOperation(node, leaks, fields);

    if (node.jp instanceof ReturnStatement) {
      // console.log("Return statement: ", node.jp.code);
      if (node.jp.children.length > 0) {
        const register = node.jp.children[0].code;
        const parent = node.jp.parent;
        const returnType = parent.prototype.returnType.code;

        if (leaks[register] !== undefined) {
          if (leaks[register].resource.type === returnType) {
            methodCallResult = this.cloneResourceLeak(leaks[register]);
            this.releaseResource(leaks, register);
          }
        }
      }
    }

    if (this.isMoveObjectOp(node.jp)) {
      // console.log("Move object: ", node.jp.code);
      const destination = node.jp.children[0].code;
      const source = node.jp.children[1].code;

      // The references from the source are copied to the destination
      // but not cleared from the source register,
      // something I didn't take into account and needs to be changed,
      // maybe by using the id when releasing a resource to release all with the same id

      if (fields[source] !== undefined) {
        fields[destination] = fields[source];

        delete fields[source];
      }

      if (leaks[source] !== undefined) {
        leaks[destination] = leaks[source];

        delete leaks[source];
      }
    }
  }

  processMethodCall(
    node,
    leaks,
    fields,
    isClosedOps,
    visitedMethods,
    nodesToVisit,
    visitedNodes,
  ) {
    const methodRef = node.jp.children[1];
    let methodNode = undefined;

    let classNode = this.app.classes.find(
      (classJp) =>
        classJp.classDescriptor.code === methodRef.parentClassDescriptor.code,
    );

    while (classNode !== undefined) {
      methodNode = this.graph.getFunction(
        classNode.classDescriptor.code +
          "->" +
          methodRef.name +
          methodRef.prototype.code,
      );
      if (methodNode !== undefined) {
        break;
      }
      classNode = this.app.classes.find(
        (classJp) =>
          classJp.classDescriptor.code === classNode.superClassDescriptor.code,
      );
    }

    if (methodNode === undefined) {
      return;
    }

    if (visitedMethods[methodRef.code] === undefined) {
      visitedMethods[methodRef.code] = [];
    }

    for (let i = visitedMethods[methodRef.code].length - 1; i >= 0; i--) {
      const visit = visitedMethods[methodRef.code][i];
      if (visit.after === undefined) {
        if (visit.acquisitionId !== node.jp.id) {
          continue;
        }

        if (methodCallResult !== undefined) {
          const nextInstruction = this.getNextInstruction(node.jp);

          if (
            nextInstruction.opCodeName !== undefined &&
            nextInstruction.opCodeName.startsWith("move-result")
          ) {
            let reg = nextInstruction.children[0].code;
            let field = undefined;
            if (methodCallResult.resource.singleInstance) {
              reg = this.getRegisterReferences(node.jp)[0];
              field = fields[reg];
            }
            this.moveLeaksDataToSafeRegister(leaks, fields, reg);
            if (field !== undefined) {
              fields[reg] = field;
            }
            leaks[reg] = this.cloneResourceLeak(methodCallResult);
            leaks[reg].acquisitionJp = node.jp;
            leaks[reg].removeJoinpoint();
            leaks[reg].addJoinpoint(node.jp);
          }

          methodCallResult = undefined;
        }

        visit.after = this.deepClone(leaks);

        return;
      }

      if (
        this.leaksObjectsAreEqual(visit.before, visit.after) &&
        Object.keys(leaks).length === 0
      ) {
        return;
      }

      if (
        visit.acquisitionId === node.jp.id &&
        this.leaksObjectsAreEqual(leaks, visit.before)
      ) {
        for (const key in leaks) {
          delete leaks[key];
        }
        for (const tmpKey in visit.after) {
          leaks[tmpKey] = this.deepClone(visit.after[tmpKey]);
        }
        return;
      }
    }

    visitedMethods[methodRef.code].push({
      acquisitionId: node.jp.id,
      before: this.deepClone(leaks),
      after: undefined,
    });

    const registers = this.getRegisterReferences(node.jp);

    let newFields = {};
    let newLeaks = {};

    for (const key of Object.keys(fields)) {
      if (registers.includes(key)) {
        newFields["p" + registers.indexOf(key)] = this.deepClone(fields[key]);
        continue;
      }

      newFields[key + "_"] = this.deepClone(fields[key]);
    }

    for (const key of Object.keys(leaks)) {
      if (registers.includes(key)) {
        newLeaks["p" + registers.indexOf(key)] = this.deepClone(leaks[key]);
        continue;
      }

      newLeaks[key + "_"] = this.deepClone(leaks[key]);
    }

    if (!methodNode.jp.isStatic && newFields["p0"] === undefined) {
      newFields["p0"] = methodNode.jp.parent.classDescriptor.code;
    }

    for (const visitedObject of visitedNodes) {
      if (visitedObject.node.jp.id === node.jp.id) {
        visitedNodes.delete(visitedObject);
        break;
      }
    }

    nodesToVisit.push({
      currNode: node,
      currLeaks: leaks,
      currFields: fields,
      currIsClosedOps: isClosedOps,
      functionVisitedNodes: visitedNodes,
    });

    nodesToVisit.push({
      currNode: methodNode,
      currLeaks: newLeaks,
      currFields: newFields,
      currIsClosedOps: {},
      functionVisitedNodes: new Set(),
    });
  }

  processResourceAcquisition(node, leaks, fields) {
    let nextInstruction = this.getNextInstruction(node.jp);

    if (
      nextInstruction.opCodeName === undefined ||
      !nextInstruction.opCodeName.startsWith("move-result")
    ) {
      console.log("Operation result not being saved: ", node.jp.code);
      return;
    }

    for (const reg in leaks) {
      if (leaks[reg].acquisitionJp.id === node.jp.id) {
        return;
      }
    }

    const register = nextInstruction.children[0].code;
    const resource = resources.find(
      (resource) =>
        resource.acquisitionMethod.includes(node.jp.children[1].name) &&
        resource.type === node.jp.children[1].prototype.returnType.code,
    );

    this.moveLeaksDataToSafeRegister(leaks, fields, register);

    this.acquireResource(leaks, register, resource, node.jp, fields);
  }

  processFieldOperation(node, leaks, fields) {
    if (
      node.jp.opCodeName === "iget-object" ||
      node.jp.opCodeName === "sget-object"
    ) {
      // console.log("Field reference: ", node.jp.code);
      const destination = node.jp.children[0].code;
      const fieldRef =
        node.jp.opCodeName === "sget-object"
          ? node.jp.children[1]
          : node.jp.children[2];

      for (const field in fields) {
        if (fields[field] === fieldRef.code) {
          if (leaks[field] !== undefined) {
            leaks[destination] = leaks[field];

            while (leaks[destination].jpList.length > 1) {
              leaks[destination].removeJoinpoint();
            }
          }

          delete leaks[field];
          delete fields[field];
        }
      }

      fields[destination] = fieldRef.code;
    }

    if (
      node.jp.opCodeName === "iput-object" ||
      node.jp.opCodeName === "sput-object"
    ) {
      // console.log("Field assignment: ", node.jp.code);
      const source = node.jp.children[0].code;
      const fieldRef =
        node.jp.opCodeName === "sput-object"
          ? node.jp.children[1]
          : node.jp.children[2];

      fields[source] = fieldRef.code;
    }
  }

  processComparison(incomingNode, childNode, leaks, isClosedOpsMap) {
    if (incomingNode.jp instanceof IfComparisonWithZero) {
      leaks = this.checkImplicitReleaseFromComparison(
        incomingNode,
        childNode,
        leaks,
        isClosedOpsMap,
      );
    }

    if (incomingNode.is(ConditionNode.TypeGuard)) {
      for (const register in leaks) {
        if (register.endsWith("_")) {
          continue;
        }

        let replacementJp = childNode.jp;
        const nextInstruction = this.getNextInstruction(childNode.jp);

        if (
          childNode.jp instanceof Label &&
          nextInstruction !== undefined &&
          nextInstruction.opCodeName === "move-exception"
        ) {
          replacementJp = nextInstruction;
        }

        leaks[register].removeJoinpoint();
        leaks[register].addJoinpoint(replacementJp);
      }
    }

    if (incomingNode.is(TryCatchNode.TypeGuard) && childNode.jp instanceof Label) {
      for (const register in leaks) {
        if (register.endsWith("_")) {
          continue;
        }

        if (leaks[register].acquisitionJp.id === incomingNode.jp.id) {
          this.releaseResource(leaks, register);
        }
      }
    }

    return leaks;
  }

  checkImplicitReleaseFromComparison(
    incomingNode,
    childNode,
    leaks,
    isClosedOpsMap,
  ) {
    if (incomingNode.jp instanceof IfComparisonWithZero) {
      const childIndex = incomingNode.nextNodes.findIndex(
        (node) => node.jp.id === childNode.jp.id,
      );

      const register = incomingNode.jp.children[0].code;
      let isAcquiredValidationOp = true;
      let regToRelease = register;

      if (isClosedOpsMap[register] !== undefined) {
        regToRelease = isClosedOpsMap[register];
        if (leaks[regToRelease] !== undefined) {
          isAcquiredValidationOp =
            leaks[regToRelease].resource.isReleasedMethod.startsWith("!");
        }
        delete isClosedOpsMap[register];
      }

      if (
        (incomingNode.jp.opCodeName === "if-eqz" &&
          childIndex === Number(!isAcquiredValidationOp)) ||
        (incomingNode.jp.opCodeName === "if-nez" &&
          childIndex === Number(isAcquiredValidationOp))
      ) {
        this.releaseResource(leaks, regToRelease);
      }
    }

    return leaks;
  }

  leaksObjectsAreEqual(obj1, obj2) {
    if (Object.keys(obj1).length !== Object.keys(obj2).length) {
      return false;
    }

    for (const register in obj1) {
      if (obj2[register] === undefined) {
        return false;
      }

      if (
        !register.endsWith("_") &&
        !this.resourceLeaksAreEqual(obj1[register], obj2[register])
      ) {
        return false;
      }

      if (obj1[register].resource !== obj2[register].resource) {
        return false;
      }

      if (obj1[register].acquisitionClass !== obj2[register].acquisitionClass) {
        return false;
      }

      if (obj1[register].acquisitionJp.id !== obj2[register].acquisitionJp.id) {
        return false;
      }

      if (obj1[register].jpList.length !== obj2[register].jpList.length) {
        return false;
      }
    }

    return true;
  }

  combineLeaksObjects(leaksObjects, commonJp) {
    let combinedLeaks = {};

    let uniqueAcquisitionIds = new Set();

    for (const leaks of leaksObjects) {
      for (const register in leaks) {
        if (uniqueAcquisitionIds.has(leaks[register].acquisitionJp.id)) {
          continue;
        }

        uniqueAcquisitionIds.add(leaks[register].acquisitionJp.id);
      }
    }

    for (const id of uniqueAcquisitionIds) {
      let leaksPerId = [];
      let safestRegister = undefined;

      for (const leaks of leaksObjects) {
        for (const register in leaks) {
          if (leaks[register].acquisitionJp.id === id) {
            leaksPerId.push(leaks[register]);

            if (safestRegister === undefined) {
              safestRegister = register;
            }

            if (register.length > safestRegister.length) {
              safestRegister = register;
            }
          }
        }
      }

      if (combinedLeaks[safestRegister] !== undefined) {
        // If they're the same type of resource on the same register
        // it doesn't matter where we acquired them, we can combine them
        if (
          !safestRegister.endsWith("_") &&
          leaksPerId[0].resource === combinedLeaks[safestRegister].resource &&
          leaksPerId[0].acquisitionClass ===
            combinedLeaks[safestRegister].acquisitionClass
        ) {
          let replacementJp = commonJp;
          const nextInstruction = this.getNextInstruction(commonJp);
          if (
            commonJp instanceof Label &&
            nextInstruction !== undefined &&
            nextInstruction.opCodeName === "move-exception"
          ) {
            replacementJp = nextInstruction;
          }

          combinedLeaks[safestRegister].removeJoinpoint();
          combinedLeaks[safestRegister].addJoinpoint(replacementJp);

          continue;
        }

        this.moveLeaksDataToSafeRegister(combinedLeaks, {}, safestRegister);
      }

      combinedLeaks[safestRegister] = this.combineResourceLeaks(
        leaksPerId,
        commonJp,
        safestRegister,
      );
    }

    return combinedLeaks;
  }

  combineResourceLeaks(leaksList, commonJp, register) {
    let combinedLeak = new ResourceLeak(
      leaksList[0].resource,
      leaksList[0].acquisitionClass,
      leaksList[0].acquisitionJp,
    );

    for (const leaks of leaksList) {
      for (let i = 0; i < leaks.jpList.length; i++) {
        let foundJp = false;
        for (const presentJp of combinedLeak.jpList) {
          if (this.joinpointsAreEqual(leaks.jpList[i], presentJp)) {
            foundJp = true;
            break;
          }
        }

        if (
          i === leaks.jpList.length - 1 &&
          this.replaceWithCommonJp(leaksList, register) &&
          commonJp instanceof Statement
        ) {
          if (
            combinedLeak.jpList.length === 0 ||
            !this.joinpointsAreEqual(combinedLeak.getLastJoinpoint(), commonJp)
          ) {
            const nextInstruction = this.getNextInstruction(commonJp);

            if (
              commonJp instanceof Label &&
              nextInstruction !== undefined &&
              nextInstruction.opCodeName === "move-exception"
            ) {
              commonJp = nextInstruction;
            }

            combinedLeak.addJoinpoint(commonJp);
          }
          continue;
        }

        if (!foundJp) {
          combinedLeak.addJoinpoint(leaks.jpList[i]);
        }
      }
    }

    return combinedLeak;
  }

  replaceWithCommonJp(leaksList, register) {
    if (leaksList.length <= 1) {
      return false;
    }

    if (register.endsWith("_")) {
      return false;
    }

    let size = undefined;

    for (const resourceLeak of leaksList) {
      if (size === undefined) {
        size = resourceLeak.jpList.length;
      }

      if (size !== resourceLeak.jpList.length) {
        return false;
      }
    }

    return true;
  }

  acquireResource(leaksObj, register, resource, jp, fields) {
    let acquisitionClass = undefined;
    if (resource.singleInstance) {
      const reg = this.getRegisterReferences(jp)[0];
      if (fields[reg] !== undefined) {
        acquisitionClass = fields[reg].slice(fields[reg].indexOf(":") + 1);
        register = reg;
      }
    }

    let foundReg = undefined;

    for (const leak in leaksObj) {
      if (
        leaksObj[leak].resource === resource &&
        this.joinpointsAreEqual(leaksObj[leak].getLastJoinpoint(), jp) &&
        leaksObj[leak].acquisitionClass === acquisitionClass
      ) {
        foundReg = leak;
        break;
      }
    }

    if (foundReg !== undefined) {
      leaksObj[foundReg].addJoinpoint(jp);
      leaksObj[register] = leaksObj[foundReg];
      delete leaksObj[foundReg];

      if (fields[foundReg] !== undefined) {
        delete fields[foundReg];
      }

      return;
    }

    leaksObj[register] = new ResourceLeak(resource, acquisitionClass);
    leaksObj[register].addJoinpoint(jp);
  }

  releaseResource(leaksObj, register) {
    if (leaksObj[register] === undefined) {
      return;
    }

    leaksObj[register].removeJoinpoint();

    if (leaksObj[register].jpList.length === 0) {
      delete leaksObj[register];
    }
  }

  isResourceAcquisition(instruction) {
    if (!this.isMethodCall(instruction)) {
      return false;
    }

    const methodRef = instruction.children[1];

    // let classNode = instruction.parent.parent;
    // let superClass = classNode.classDescriptor.code;

    // while (classNode !== undefined) {
    //   if (classNode.superClassDescriptor !== undefined) {
    //     superClass = classNode.superClassDescriptor.code;
    //     classNode = classNode.superClassDescriptor.decl;
    //     continue;
    //   }

    //   break;
    // }

    for (const resource of resources) {
      if (
        // resource.acquisitionClass.includes(
        //   methodRef.parentClassDescriptor.code,
        // ) &&
        // !resource.managingClassAdapters.includes(superClass) &&
        resource.acquisitionMethod.includes(methodRef.name) &&
        resource.type === methodRef.prototype.returnType.code
      ) {
        return true;
      }
    }

    return false;
  }

  isResourceRelease(instruction, leaks) {
    if (!this.isMethodCall(instruction)) {
      return false;
    }

    const rList = instruction.children[0];
    // const register = rList.children[0];
    let register = undefined;

    // The register isn't gonna be the first in startManagingResource,
    // this should probably be declared in the resource's details
    for (const reg of rList.children) {
      if (leaks[reg.code] !== undefined) {
        register = reg;
        break;
      }
    }

    if (register === undefined) {
      return false;
    }

    // if (register === undefined || leaks[register.code] === undefined ) {
    //   return false;
    // }

    const methodRef = instruction.children[1];

    let classType = methodRef.parentClassDescriptor.decl;
    let superClass = methodRef.parentClassDescriptor.code;

    while (classType !== undefined) {
      if (classType.superClassDescriptor !== undefined) {
        superClass = classType.superClassDescriptor.code;
        classType = classType.superClassDescriptor.decl;
        continue;
      }

      break;
    }

    if (
      leaks[register.code].resource.releaseClass.includes(superClass) &&
      leaks[register.code].resource.releaseMethod.includes(methodRef.name) &&
      leaks[register.code].jpList.length > 0
    ) {
      return true;
    }

    return false;
  }

  isHeldValidation(instruction, leaks) {
    if (!this.isMethodCall(instruction)) {
      return false;
    }

    const register = this.getRegisterReferences(instruction)[0];

    if (register === undefined || leaks[register] === undefined) {
      return false;
    }

    const methodRef = instruction.children[1];

    let classType = methodRef.parentClassDescriptor.decl;
    let superClass = methodRef.parentClassDescriptor.code;

    while (classType !== undefined) {
      if (classType.superClassDescriptor !== undefined) {
        superClass = classType.superClassDescriptor.code;
        classType = classType.superClassDescriptor.decl;
        continue;
      }

      break;
    }

    if (
      leaks[register].jpList.length > 0 &&
      leaks[register].resource.type === superClass &&
      (leaks[register].resource.isReleasedMethod === methodRef.name ||
        leaks[register].resource.isReleasedMethod === "!" + methodRef.name)
    ) {
      return true;
    }

    return false;
  }

  moveLeaksDataToSafeRegister(leaks, fields, register) {
    let safeRegister = register + "_";

    while (
      leaks[safeRegister] !== undefined ||
      fields[safeRegister] !== undefined
    ) {
      safeRegister += "_";
    }

    if (leaks[register] !== undefined) {
      leaks[safeRegister] = leaks[register];
      delete leaks[register];
    }

    if (fields[register] !== undefined) {
      fields[safeRegister] = fields[register];
      delete fields[register];
    }
  }

  getUnusedRegisterName(register, object) {
    let safeRegister = register + "_";

    while (object[safeRegister] !== undefined) {
      safeRegister += "_";
    }

    return safeRegister;
  }

  getRegisterReferences(instruction) {
    let registers = [];

    const handleRegisterRange = (child) => {
      if (child.children.length > 0) {
        const namingScheme = child.children[0].code[0];
        const regStart = parseInt(child.children[0].code.substring(1));
        const regEnd =
          child.children[1] !== undefined
            ? parseInt(child.children[1].code.substring(1))
            : regStart;

        for (let i = regStart; i <= regEnd; i++) {
          registers.push(namingScheme + i);
        }
      }
    };

    for (const child of instruction.children) {
      if (child instanceof RegisterReference) {
        registers.push(child.code);
      } else if (child instanceof RegisterList) {
        for (const reg of child.children) {
          registers.push(reg.code);
        }
      } else if (child instanceof RegisterRange) {
        handleRegisterRange(child);
      }
    }

    return registers;
  }

  isMethodCall(instruction) {
    // Could probably just check if reference type is method
    if (
      instruction.opCodeName === "invoke-virtual/range" ||
      instruction.opCodeName === "invoke-super/range" ||
      instruction.opCodeName === "invoke-direct/range" ||
      instruction.opCodeName === "invoke-static/range" ||
      instruction.opCodeName === "invoke-interface/range" ||
      instruction.opCodeName === "invoke-virtual" ||
      instruction.opCodeName === "invoke-super" ||
      instruction.opCodeName === "invoke-direct" ||
      instruction.opCodeName === "invoke-static" ||
      instruction.opCodeName === "invoke-interface"
    ) {
      const methodRef = instruction.children[1];
      if (!(methodRef instanceof MethodReference)) {
        return false;
      }

      return true;
    }

    return false;
  }

  isMoveObjectOp(instruction) {
    return (
      instruction.opCodeName === "move-object" ||
      instruction.opCodeName === "move-object/from16" ||
      instruction.opCodeName === "move-object/16"
    );
  }

  resourceLeaksAreEqual(rl1, rl2) {
    if (rl1 === rl2) {
      return true;
    }

    if (rl1 === null || rl2 === null) {
      return false;
    }

    if (rl1.resource !== rl2.resource) {
      return false;
    }

    if (rl1.acquisitionClass !== rl2.acquisitionClass) {
      return false;
    }

    if (rl1.acquisitionJp.id !== rl2.acquisitionJp.id) {
      return false;
    }

    // if (rl1.jpList.length !== rl2.jpList.length) {
    //   return false;
    // }

    // for (let i = 0; i < rl1.jpList.length; i++) {
    if (
      !this.joinpointsAreEqual(rl1.getLastJoinpoint(), rl2.getLastJoinpoint())
    ) {
      return false;
    }
    // }

    return true;
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

  joinpointsAreEqual(jp1, jp2) {
    if (jp1 === jp2) {
      return true;
    }

    if (jp1 === null || jp2 === null) {
      return false;
    }

    if (jp1 === undefined || jp2 === undefined) {
      return false;
    }

    if (jp1.id !== jp2.id) {
      return false;
    }

    if (jp1.code !== jp2.code) {
      return false;
    }

    if (jp1.children.length !== jp2.children.length) {
      return false;
    }

    for (let i = 0; i < jp1.children.length; i++) {
      if (!this.joinpointsAreEqual(jp1.children[i], jp2.children[i])) {
        return false;
      }
    }

    return true;
  }

  deepClone(obj) {
    if (obj === null || typeof obj !== "object" || obj instanceof Joinpoint) {
      return obj;
    }

    if (obj instanceof ResourceLeak) {
      return this.cloneResourceLeak(obj);
    }

    const clone = Array.isArray(obj) ? [] : {};

    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clone[key] = this.deepClone(obj[key]);
      }
    }

    return clone;
  }

  cloneResourceLeak(rl) {
    const clone = new ResourceLeak(
      rl.resource,
      rl.acquisitionClass,
      rl.acquisitionJp,
    );

    for (let i = 0; i < rl.jpList.length; i++) {
      clone.addJoinpoint(rl.jpList[i]);
    }

    return clone;
  }
}

export default LeaksDetection;

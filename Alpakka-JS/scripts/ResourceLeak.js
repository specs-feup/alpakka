class ResourceLeak {
  constructor(resource, acquisitionClass, acquisitionJp = undefined) {
    this.resource = resource;
    this.jpList = [];
    this.acquisitionClass = acquisitionClass;
    this.acquisitionJp = acquisitionJp;
  }

  addJoinpoint(joinpoint) {
    if (this.resource.singleInstance) {
      this.jpList.pop();
    }
    this.jpList.push(joinpoint);
    if (this.acquisitionJp === undefined) {
      this.acquisitionJp = joinpoint;
    }
  }

  removeJoinpoint() {
    this.jpList.pop();
  }

  getLastJoinpoint() {
    return this.jpList[this.jpList.length - 1];
  }
}

export default ResourceLeak;

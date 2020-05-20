export class Tool {
  constructor(
    public type?: any,
    public version?: string,
    public os?: string,
    public url?: string,
    public archive?: string
  ) {
    this.type = type ? type : null;
    this.version = version ? version : null;
    this.os = os ? os : null;
    this.url = url ? url : null;
    this.archive = archive ? archive : null;
  }
}

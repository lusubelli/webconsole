export class Repository {
  constructor(
    public type?: string,
    public url?: string,
    public name?: string,
    public username?: string
  ) {
    this.type = type ? type : null;
    this.url = url ? url : null;
    this.name = name ? name : null;
    this.username = username ? username : null;
  }
}

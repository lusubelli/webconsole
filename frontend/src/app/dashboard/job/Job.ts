class Build {
  constructor(
    public id?: any,
    public job?: string,
    public javaId?: string,
    public mavenId?: string,
    public gitRepository?: string,
    public branch?: string,
    public repositoryId?: string,
    public gitTag?: string,
    public startTime?: Date,
    public commitMessage?: string,
    public state?: string
  ) {
    this.id = id ? id : null;
    this.job = job ? job : null;
    this.javaId = javaId ? javaId : null;
    this.mavenId = mavenId ? mavenId : null;
    this.gitRepository = gitRepository ? gitRepository : null;
    this.branch = branch ? branch : null;
    this.repositoryId = repositoryId ? repositoryId : null;
    this.gitTag = gitTag ? gitTag : null;
    this.startTime = startTime ? startTime : null;
    this.commitMessage = commitMessage ? commitMessage : null;
    this.state = state ? state : null;
  }
}

class Repository {
  constructor(
    public branch?: any,
    public repositoryId?: string
  ) {
    this.branch = branch ? branch : null;
    this.repositoryId = repositoryId ? repositoryId : null;
  }
}

export class Job {
  constructor(
    public project?: any,
    public job?: string,
    public javaId?: string,
    public mavenId?: string,
    public gitRepository?: string,
    public snapshot?: Repository,
    public release?: Repository,
    public builds?: Build[]
  ) {
    this.project = project ? project : null;
    this.job = job ? job : null;
    this.javaId = javaId ? javaId : null;
    this.mavenId = mavenId ? mavenId : null;
    this.gitRepository = gitRepository ? gitRepository : null;
    this.snapshot = snapshot ? snapshot : null;
    this.release = release ? release : null;
    this.builds = builds ? builds : null;
  }
}

import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Job} from "../Job";
import {JobOverlayService} from "../job-overlay.service";
import {JobService} from "../job.service";
import {Tool} from "../../../Tool";
import {ToolService} from "../tool.service";


@Component({
  selector: 'job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.scss']
})
export class JobListComponent implements OnInit {

  @Output()
  selectBuild: EventEmitter<any> = new EventEmitter<any>();

  _jobs: Job[];
  mavens: Tool[];
  javas: Tool[];

  constructor(private toolService: ToolService,
              private jobService: JobService,
              private jobOverlayService: JobOverlayService) {
  }

  ngOnInit(): void {
    this.loadJobs();
    this.loadTools();
  }

  loadTools(): void {
    this.toolService.availableTools()
      .subscribe((tools: Tool[]) => {
        if(tools) {
          this.mavens = [];
          this.javas = [];
          for(var i=0; i <tools.length; i++) {
            if(tools[i].type === "maven") {
              this.mavens.push(tools[i]);
            } else if (tools[i].type === "java") {
              this.javas.push(tools[i]);
            }
          }
        }
      });
  }

  loadJobs(): void {
    this.jobService.showJobs()
      .subscribe((jobs: Job[]) => {
        this._jobs = jobs;
      });
  }

  projects() {
    var projects = {};
    if (this._jobs) {
      this._jobs.forEach(function (a) {
        projects [a.project] = projects [a.project] || [];
        projects [a.project].push(a);
      });
    }
    return projects;
  }

  runBuild(job: Job): void {
    this.jobService.runBuild({
      project: job.project,
      job: job.job,
      javaId: job.javaId,
      mavenId: job.mavenId,
      gitRepository: job.gitRepository,
      repository: {
        branch: job.snapshot.branch,
        repositoryId: job.snapshot.repositoryId
      }
    }).subscribe((result: boolean) => {
      this.loadJobs();
    });
  }

  openRunReleaseModal(job: Job): void {
    this.jobOverlayService.openReleaseModal((releaseVersion: string) => {
      this.jobService.runBuild({
        project: job.project,
        job: job.job,
        javaId: job.javaId,
        mavenId: job.mavenId,
        gitRepository: job.gitRepository,
        gitTag: releaseVersion,
        repository: {
          branch: job.snapshot.branch,
          repositoryId: job.snapshot.repositoryId
        }
      }).subscribe((result: boolean) => {
        this.loadJobs();
      });
      this.jobOverlayService.closeReleaseModal();
    });
  }

  openCreateJobModal() {
    this.jobOverlayService.openCreateJobModal((job: Job) => {
      this.loadJobs();
      this.jobOverlayService.closeCreateJobModal();
    }, this.mavens, this.javas);
  }

  setSelectedBuild(build) {
    this.selectBuild.emit(build);
  }

}

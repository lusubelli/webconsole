import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {JobService} from "../../job.service";

@Component({
  selector: 'build-console',
  templateUrl: './build-console.component.html',
  styleUrls: ['./build-console.component.scss']
})
export class BuildConsoleComponent implements OnInit {

  _build;

  logs: string;

  constructor(private jobService: JobService) {}

  ngOnInit(): void {
    this.readLogs(this.build);
  }

  private readLogs(currentBuild) {
    if (currentBuild) {
      this.jobService
        .readBuildLogs(currentBuild)
        .subscribe(log => {
          this.logs = log;
        });
    }
  }

  @Input()
  set build(build: any) {
    this._build = build;
    this.readLogs(this._build);
  }

  get build(): any {
    return this._build;
  }

}

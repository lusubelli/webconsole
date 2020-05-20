import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Job} from "../Job";
import {JobService} from "../job.service";
import {JobOverlayService} from "../job-overlay.service";
import {Tool} from "../../../Tool";

@Component({
  selector: 'job-create',
  templateUrl: './job-create.component.html',
  styleUrls: ['./job-create.component.scss']
})
export class JobCreateComponent implements OnInit {

  private urlRegex: RegExp = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/;

  @Input()
  mavens: Tool[];// = [ { "name": "maven-3.6.3", "version" : "apache-maven-3.6.3"} ];
  @Input()
  javas: Tool[];
  @Output()
  close: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output()
  submit: EventEmitter<boolean> = new EventEmitter<boolean>();

  createJobForm: FormGroup;
  snapshotFormGroup: FormGroup;
  releaseFormGroup: FormGroup;

  serverError: string;

  constructor(private jobService: JobService) { }

  ngOnInit(): void {
    this.snapshotFormGroup = new FormGroup({
      branch: new FormControl('master', [Validators.required, Validators.minLength(3), Validators.maxLength(25)])
    });
    this.releaseFormGroup = new FormGroup({
      branch: new FormControl('master', [Validators.required, Validators.minLength(3), Validators.maxLength(25)])
    });
    this.createJobForm = new FormGroup({
      project: new FormControl('Project', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]),
      job: new FormControl('Job', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]),
      javaId: new FormControl('', [Validators.required]),
      mavenId: new FormControl('', [Validators.required]),
      gitRepository: new FormControl('https://github.com/lusubelli/backend.git', [Validators.required, Validators.pattern(this.urlRegex)]),
      snapshot: this.snapshotFormGroup,
      release: this.releaseFormGroup
    });
  }

  createJob() {
    if (this.createJobForm.valid) {
      let job: Job = this.createJobForm.value;
      this.jobService.createJob(job)
        .subscribe((created: boolean) => {
          this.submit.emit(true);
        }, (error: string) => {
          this.serverError = error;
        });
    }
  }

  cancel() {
    this.close.emit(true);
  }

}

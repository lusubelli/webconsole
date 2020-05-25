import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'release',
  templateUrl: './release.component.html',
  styleUrls: ['./release.component.scss']
})
export class ReleaseComponent {

  @Output()
  close: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output()
  submit: EventEmitter<string> = new EventEmitter<string>();

  runReleaseBuildForm = new FormGroup({
    releaseVersion: new FormControl('1.0.0')
  });

  runReleaseBuild() {
    this.submit.emit(this.runReleaseBuildForm.value.releaseVersion);
  }

  cancel() {
    this.close.emit(true);
  }

}

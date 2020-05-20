import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {Repository} from "../Repository";
import {Job} from "../../../dashboard/job/Job";

@Component({
  selector: 'repository-create',
  templateUrl: './repository-create.component.html',
  styleUrls: ['./repository-create.component.scss']
})
export class RepositoryCreateComponent implements OnInit {

  @Input()
  _type: string;
  @Output()
  close: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output()
  submit: EventEmitter<Repository> = new EventEmitter<Repository>();

  _createRepositoryForm: FormGroup;

  ngOnInit(): void {
    this._createRepositoryForm = new FormGroup({
      type: new FormControl(this._type),
      url: new FormControl('http://localhost:8081'),
      name: new FormControl('snapshots'),
      username: new FormControl('admin'),
      password: new FormControl('admin123')
    });
  }

  createOrUpdateRepository() {
    this.submit.emit(this._createRepositoryForm.value);
  }

  cancel() {
    this.close.emit(true);
  }

}

import {Component, ViewContainerRef} from '@angular/core';
import {JobOverlayService} from "./dashboard/job/job-overlay.service";
import {RepositoryOverlayService} from "./administration/repository/repository-overlay.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';
  constructor(
    private viewContainerRef: ViewContainerRef,
    private jobOverlayService: JobOverlayService,
    private repositoryOverlayService: RepositoryOverlayService
  ) {
    this.jobOverlayService.viewContainerRef = viewContainerRef;
    this.repositoryOverlayService.viewContainerRef = viewContainerRef;
  }

}

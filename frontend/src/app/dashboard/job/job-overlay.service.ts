import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {OverlayService} from "../../core/overlay.service";
import {OverlayConfig} from "@angular/cdk/overlay";
import {ComponentPortal} from "@angular/cdk/portal";
import {JobCreateComponent} from "./create/job-create.component";
import {ReleaseComponent} from "./build/release/release.component";
import {Tool} from "../../Tool";

@Injectable({
  providedIn: 'root'
})
export class JobOverlayService {

  private _viewContainerRef: ViewContainerRef;

  set viewContainerRef(value: ViewContainerRef) {
    this._viewContainerRef = value;
  }

  constructor(
    public overlayService: OverlayService
  ) {
  }

  openCreateJobModal(createJob, mavens: Tool[], javas: Tool[]) {

    const config = new OverlayConfig({
      hasBackdrop: true,
      backdropClass: "modal__backdrop",
      panelClass: ["modal__panel", "modal__panel"]
    });
    const conditionsGeneralesUtilisationPortal = new ComponentPortal(JobCreateComponent, this._viewContainerRef);
    let configureComponent = (componentRef: ComponentRef<JobCreateComponent>) => {
      componentRef.instance.mavens = mavens;
      componentRef.instance.javas = javas;
      componentRef.instance.close.subscribe(() => this.overlayService.close());
      componentRef.instance.submit.subscribe(job => createJob(job));
    };
    this.overlayService.open(conditionsGeneralesUtilisationPortal, configureComponent, config, () => this.overlayService.close());
  }

  closeCreateJobModal() {
    this.overlayService.close();
  }

  openReleaseModal(release) {
    const config = new OverlayConfig({
      hasBackdrop: true,
      backdropClass: "modal__backdrop",
      panelClass: ["modal__panel", "modal__panel"]
    });
    const conditionsGeneralesUtilisationPortal = new ComponentPortal(ReleaseComponent, this._viewContainerRef);
    let configureComponent = (componentRef: ComponentRef<ReleaseComponent>) => {
      componentRef.instance.close.subscribe(() => this.overlayService.close());
      componentRef.instance.submit.subscribe(releaseVersion => release(releaseVersion));
    };
    this.overlayService.open(conditionsGeneralesUtilisationPortal, configureComponent, config, () => this.overlayService.close());
  }

  closeReleaseModal() {
    this.overlayService.close();
  }

}

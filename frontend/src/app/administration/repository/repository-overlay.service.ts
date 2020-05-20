import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {OverlayService} from "../../core/overlay.service";
import {OverlayConfig} from "@angular/cdk/overlay";
import {ComponentPortal} from "@angular/cdk/portal";
import {RepositoryCreateComponent} from "./create/repository-create.component";

@Injectable({
  providedIn: 'root'
})
export class RepositoryOverlayService {

  private _viewContainerRef: ViewContainerRef;

  set viewContainerRef(value: ViewContainerRef) {
    this._viewContainerRef = value;
  }

  constructor(
    public overlayService: OverlayService
  ) {
  }

  openCreateRepositoryModal(type: string, createOrUpdateRepository) {
    const config = new OverlayConfig({
      hasBackdrop: true,
      backdropClass: "modal__backdrop",
      panelClass: ["modal__panel", "modal__panel"]
    });
    const conditionsGeneralesUtilisationPortal = new ComponentPortal(RepositoryCreateComponent, this._viewContainerRef);
    let configureComponent = (componentRef: ComponentRef<RepositoryCreateComponent>) => {
      componentRef.instance._type = type;
      componentRef.instance.close.subscribe(() => this.overlayService.close());
      componentRef.instance.submit.subscribe(repository => createOrUpdateRepository(repository));
    };
    this.overlayService.open(conditionsGeneralesUtilisationPortal, configureComponent, config, () => this.overlayService.close());
  }

  closeCreateRepositoryModal() {
    this.overlayService.close();
  }

}

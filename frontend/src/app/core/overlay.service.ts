import {ComponentRef, Inject, Injectable} from '@angular/core';
import {Overlay, OverlayConfig, OverlayRef} from '@angular/cdk/overlay';
import {ComponentPortal} from '@angular/cdk/portal';
import {WINDOW} from "./window.service";

@Injectable({
  providedIn: 'root'
})
export class OverlayService {
  private _overlayRef: OverlayRef;
  private _defaultConfig = new OverlayConfig({
    hasBackdrop: true,
    backdropClass: 'modal__backdrop',
    panelClass: 'modal__panel'
  });
  private _modalPanelFadedOutClass = 'modal__panel--faded-out';
  private _panelDeletionDelay: number = 150;

  private _config: OverlayConfig;

  constructor(private overlay: Overlay, @Inject(WINDOW) private window: Window) { }

  public open<T>(componentPortal: ComponentPortal<T>, configureComponent: (componentRef: ComponentRef<T>) => void, config: OverlayConfig = this._defaultConfig, onBackdropClick?: Function) {
    this._config = config;
    const positionStrategy = this.overlay.position()
      .global()
      .centerHorizontally()
      .centerVertically();
    const dialogConfig = { ...positionStrategy, ...config };
    this._overlayRef = this.overlay.create(dialogConfig);
    this._overlayRef.addPanelClass(this._modalPanelFadedOutClass);
    this.window.requestAnimationFrame(() => {
      this._overlayRef.removePanelClass(this._modalPanelFadedOutClass);
    });
    const componentRef: ComponentRef<T> = this._overlayRef.attach(componentPortal);
    configureComponent(componentRef);
    componentRef.changeDetectorRef.detectChanges();
    this._overlayRef.backdropClick().subscribe(() => onBackdropClick ? onBackdropClick() : this.close());
  }

  public close(): void {
    this._overlayRef.addPanelClass(this._modalPanelFadedOutClass);
    this._overlayRef.overlayElement.addEventListener('transitionstart', () => {
      return setTimeout(() => this._overlayRef.dispose(), this._panelDeletionDelay);
    });
  }
}

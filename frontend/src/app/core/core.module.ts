import {NgModule} from "@angular/core";
import {AppComponent} from "../app.component";
import {WINDOW_PROVIDERS} from "./window.service";
import {OverlayModule} from "@angular/cdk/overlay";

@NgModule({
    declarations: [],
    imports: [OverlayModule],
    providers: [WINDOW_PROVIDERS],
    bootstrap: [AppComponent]
})
export class CoreModule {}

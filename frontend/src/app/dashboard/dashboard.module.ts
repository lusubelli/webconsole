import {NgModule} from "@angular/core";
import {AppComponent} from "../app.component";
import {CommonModule} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {JobCreateComponent} from "./job/create/job-create.component";
import {JobListComponent} from "./job/list/job-list.component";
import {JobService} from "./job/job.service";
import {DashboardContainer} from "./dashboard.container";
import {ReleaseComponent} from "./job/build/release/release.component";
import {BuildConsoleComponent} from "./job/build/console/build-console.component";
import {ToolService} from "./job/tool.service";

@NgModule({
  declarations: [
    DashboardContainer,
    JobCreateComponent,
    JobListComponent,
    BuildConsoleComponent,
    ReleaseComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    ReactiveFormsModule
  ],
  providers: [
    JobService,
    ToolService
  ],
  exports: [
    DashboardContainer
  ],
  bootstrap: [AppComponent]
})
export class DashboardModule { }

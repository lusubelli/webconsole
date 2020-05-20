import {NgModule} from "@angular/core";
import {AppComponent} from "../app.component";
import {RepositoryCreateComponent} from "./repository/create/repository-create.component";
import {RepositoryListComponent} from "./repository/list/repository-list.component";
import {RepositoryService} from "./repository/repository.service";
import {AdministrationContainer} from "./administration.container";
import {CommonModule} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";

@NgModule({
  declarations: [
    AdministrationContainer,
    RepositoryCreateComponent,
    RepositoryListComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    ReactiveFormsModule
  ],
  providers: [
    RepositoryService
  ],
  exports: [
    AdministrationContainer
  ],
  bootstrap: [AppComponent]
})
export class AdministrationModule { }

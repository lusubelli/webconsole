import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {CoreModule} from "./core/core.module";
import {AdministrationModule} from "./administration/administration.module";
import {DashboardModule} from "./dashboard/dashboard.module";
import {AppRoutingModule} from "./app-routing.module";
import {BannerComponent} from "./banner/banner.component";
import {NavigationComponent} from "./navigation/navigation.component";

@NgModule({
  declarations: [
    AppComponent,
    BannerComponent,
    NavigationComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    CoreModule,
    AppRoutingModule,
    AdministrationModule,
    DashboardModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

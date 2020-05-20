import {NgModule} from "@angular/core";

import {RouterModule, Routes} from '@angular/router';
import {DashboardContainer} from "./dashboard/dashboard.container";
import {AdministrationContainer} from "./administration/administration.container";

export const appRouteList: Routes = [
  {
    path: 'administration',
    component: AdministrationContainer
  },
  {
    path: 'dashboard',
    component: DashboardContainer
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];

@NgModule({
  exports: [
    RouterModule
  ],
  imports: [
    RouterModule.forRoot(appRouteList)
  ]
})
export class AppRoutingModule {}

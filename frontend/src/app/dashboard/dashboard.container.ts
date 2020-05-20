import {Component} from '@angular/core';

@Component({
  selector: 'dashboard-container',
  templateUrl: './dashboard.container.html',
  styleUrls: ['./dashboard.container.scss']
})
export class DashboardContainer {

  selectedBuild: any;

  setSelectedBuild(build: any) {
    this.selectedBuild = build;
  }

}

import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {Router} from "@angular/router";
import {NavigationLink} from "./navigation-link";

@Component({
  selector: 'navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit{
  _navigationLinks: Array<NavigationLink>;
  private _isMenuActive$: BehaviorSubject<boolean> = new BehaviorSubject(true);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this._navigationLinks = [
      new NavigationLink('Dashboard', 'fa fa-home', ['dashboard']),
      new NavigationLink('Administration', 'fa fa-cog', ['administration'])
    ];
  }

  isElementActive(route: any[]): boolean {
    return this.router.isActive(this.router.createUrlTree(route), false);
  }

}

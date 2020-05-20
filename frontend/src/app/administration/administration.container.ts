import {Component, OnInit} from '@angular/core';
import {Repository} from "./repository/Repository";
import {RepositoryService} from "./repository/repository.service";

@Component({
  selector: 'administration-container',
  templateUrl: './administration.container.html',
  styleUrls: ['./administration.container.scss']
})
export class AdministrationContainer implements OnInit {

  _repositories: Repository[] = [];

  constructor(private repositoryService: RepositoryService) { }

  ngOnInit(): void {
    this.listRepository();
  }

  listRepository(): void {
    this.repositoryService.listRepositories()
      .subscribe((repositories: Repository[]) => {
        this._repositories = repositories;
      });
  }

  createRepository(repository: Repository): void {
    this.repositoryService.createRepository(repository)
      .subscribe((result: boolean) => {
        this.listRepository();
      });
  }

}

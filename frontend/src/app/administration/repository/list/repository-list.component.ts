import {Component, OnInit} from '@angular/core';
import {Repository} from "../Repository";
import {RepositoryService} from "../repository.service";
import {RepositoryOverlayService} from "../repository-overlay.service";

@Component({
  selector: 'repository-list',
  templateUrl: './repository-list.component.html',
  styleUrls: ['./repository-list.component.scss']
})
export class RepositoryListComponent implements OnInit {

  _snapshotRepository: Repository;
  _releaseRepository: Repository;

  constructor(private repositoryService: RepositoryService,
              private repositoryOverlayService: RepositoryOverlayService) { }

  ngOnInit(): void {
    this.listRepository();
  }

  listRepository(): void {
    this.repositoryService.listRepositories()
      .subscribe((repositories: Repository[]) => {
        this._snapshotRepository = this.findFirstRepositoryByType(repositories, 'SNAPSHOT');
        this._releaseRepository = this.findFirstRepositoryByType(repositories, 'RELEASE');
      });
  }

  openCreateRepositoryModal(type: string): void {
    this.repositoryOverlayService.openCreateRepositoryModal(type, (repository: Repository) => {
      this.repositoryService.createRepository(repository)
        .subscribe((result: boolean) => {
          this.listRepository();
        });
      this.repositoryOverlayService.closeCreateRepositoryModal();
    });
  }

  findFirstRepositoryByType(repositories: Repository[], type: string): Repository {
      if (repositories) {
        return repositories
          .filter(repository => repository.type === type)[0];
      }
  }

}

import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from "@angular/core";
import {Observable, throwError} from "rxjs";
import {map} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {Repository} from "./Repository";

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  constructor(private httpClient: HttpClient) { }

  listRepositories(): Observable<Repository[]> {
    return this.httpClient.get(`${environment.api}/repository`, {observe: "response"})
      .pipe(
        map((reponse: HttpResponse<Repository[]>) => {
          if (reponse.status === 200) {
            return reponse.body;
          } else {
            throwError("Impossible to get repository list");
          }
        })
      );
  }

  createRepository(repository): Observable<boolean> {
    return this.httpClient.post(`${environment.api}/repository`, repository, {observe: "response"})
      .pipe(
        map((reponse: HttpResponse<boolean>) => {
          if (reponse.status === 200) {
            return true;
          } else {
            throwError("Impossible to get build list");
          }
        })
      );
  }

}

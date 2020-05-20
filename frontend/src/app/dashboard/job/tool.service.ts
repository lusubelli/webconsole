import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Observable, of, throwError} from "rxjs";
import {Job} from "./Job";
import {environment} from "../../../environments/environment";
import {catchError, map} from "rxjs/operators";
import {Tool} from "../../Tool";

@Injectable({
  providedIn: 'root'
})
export class ToolService {

  constructor(private httpClient: HttpClient) { }

  availableTools(): Observable<Tool[] | any> {
    return this.httpClient.get(`${environment.api}/tool`, {observe: "response"})
      .pipe(
        map((httpResponse: HttpResponse<Job[]>) => {
          if (httpResponse.status === 200) {
            return httpResponse.body;
          }
        }),
        catchError(e => this.handleError(e, "Impossible to load tools")));
  }

  handleError(error: HttpErrorResponse, message: string): Observable<Error> {
    let errorMessage = 'Unknown error!';
    if (error.error instanceof ErrorEvent) {
      // Client-side errors
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side errors
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(message);
  }

}

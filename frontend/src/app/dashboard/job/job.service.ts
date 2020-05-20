import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Observable, of, throwError} from "rxjs";
import {Job} from "./Job";
import {environment} from "../../../environments/environment";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class JobService {

  constructor(private httpClient: HttpClient) { }

  showJobs(): Observable<Job[] | any> {
    return this.httpClient.get(`${environment.api}/job`, {observe: "response"})
      .pipe(
        map((httpResponse: HttpResponse<Job[]>) => {
          if (httpResponse.status === 200) {
            return httpResponse.body;
          }
        }),
        catchError(e => this.handleError(e, "Impossible to load jobs")));
  }

  runBuild(build): Observable<boolean | any> {
    return this.httpClient.post(`${environment.api}/build`, build, {observe: "response"})
      .pipe(
        map((httpResponse: HttpResponse<boolean>) => {
          if (httpResponse.status === 200) {
            return true;
          }
        }),
        catchError(e => this.handleError(e, "Impossible to run build")));
  }

  readBuildLogs(build): Observable<string | any> {

    return new Observable(emitter=> {
      var xhttp = new XMLHttpRequest();
      xhttp.onprogress  = function() {
        emitter.next(xhttp.responseText);
      };
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
          emitter.next(xhttp.responseText);
          emitter.complete();
        }
      };
      xhttp.open("GET", `${environment.api}/job/${build.jobName}/build/${build.id}/logs`, true);
      xhttp.send();
    }).pipe();


/*
    return this.httpClient.get(`${environment.api}/job/${build.jobName}/build/${build.id}/logs`, {observe: "response"})
      .pipe(
        map((httpResponse: HttpResponse<string>) => {
          if (httpResponse.status === 200) {
            return true;
          }
        }),
        catchError(e => this.handleError(e, "Impossible to read build logs")));*/
  }

  createJob(job): Observable<boolean | any> {
    return this.httpClient.post(`${environment.api}/job`, job, {observe: "response"})
      .pipe(
        map((httpResponse: HttpResponse<boolean>) => {
          if (httpResponse.status === 200) {
            return true;
          }
        }),
        catchError(e => this.handleError(e, "Impossible to create job")));
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

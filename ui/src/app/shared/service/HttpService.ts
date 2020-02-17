
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class HttpService {

  constructor(private httpClient: HttpClient) { }

  get(url: string, withCred: boolean = true): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      withCredentials: withCred
    };
    return this.httpClient.get(url, httpOptions);
  }

  post(url: string, body: any, withCred: boolean = true): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      withCredentials: withCred
    };
    return this.httpClient.post(url, body, httpOptions);
  }
}

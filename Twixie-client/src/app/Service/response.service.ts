import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import Response from '../Model/response.model';

@Injectable({
  providedIn: 'root'
})
export class ResponseService {

  constructor(private _httpClient: HttpClient) { }

  addResponse(response: any): Observable<Response> {
    return this._httpClient.post<Response>(
      'http://localhost:8080/api/response/addResponse',
      response
    );
  }

  getResponseList(postId:number): Observable<Response[]> {
    return this._httpClient.get<Response[]>(
      `http://localhost:8080/api/response/getResponsesByParentPostID/${postId}`
    );
  }

  deleteResponse(responseId: number): Observable<any>{
    return this._httpClient.delete(`http://localhost:8080/api/response/deleteResponse/${responseId}`)
  }

}

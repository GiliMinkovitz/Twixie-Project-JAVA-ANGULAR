import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import Users from '../Model/users.model';
import Follow from '../Model/follow.model';
@Injectable({
  providedIn: 'root',
})
export class FollowService {
  constructor(private _httpClient: HttpClient) {}

  getFollowers(): Observable<Users[]> {
    return this._httpClient.get<Users[]>(
      `http://localhost:8080/api/follow/getFollowers`
    );
  }

  getFollowees(): Observable<Users[]> {
    return this._httpClient.get<Users[]>(
      `http://localhost:8080/api/follow/getFollowees`
    );
  }

  removeFollow(id: number): Observable<any> {
    return this._httpClient.delete(
      `http://localhost:8080/api/follow/removeFollow/${id}`
    );
  }

  addFollow(followeeId: number): Observable<Follow> {
    return this._httpClient.post<Follow>(
      `http://localhost:8080/api/follow/addFollow/${followeeId}`,
      null
    );
  }

  isFollowing(followeeId: number): Observable<boolean> {
    return this._httpClient.get<boolean>(
      `http://localhost:8080/api/follow/isFollowing/${followeeId}`
    );
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LikesService {
  constructor(private _HttpClient: HttpClient) {}

  getAmountOfLikesForPost(postId: number, type: boolean): Observable<number> {
    return this._HttpClient.get<number>(
      `http://localhost:8080/api/likes/getAmountOfLikes/${postId}`,
      { params: { type: type.toString() } }
    );
  }

  addLike(postId: number, type: boolean): Observable<any> {
    const like = {
      parentPostLiked: { postId: postId },
      type: type
    };
    // מחזיר את ה-Observable
    return this._HttpClient.post(
      'http://localhost:8080/api/likes/addLike',
      like,
      { responseType: 'text' }
    );
  }
}

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import Post from '../Model/post.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private _httpClient: HttpClient) {}

  getMyPosts(): Observable<Post[]> {
    return this._httpClient.get<Post[]>(
      `http://localhost:8080/api/post/getMyPostsByUser`
    );
  }

  getAllPosts(): Observable<Post[]> {
    return this._httpClient.get<Post[]>(
      'http://localhost:8080/api/post/getAllPosts'
    );
  }

  addPostWithImage(formData: FormData): Observable<Post> {
    return this._httpClient.post<Post>(
      'http://localhost:8080/api/post/addPost',
      formData
    );
  }

  getPostById(postId: number): Observable<Post> {
    return this._httpClient.get<Post>(
      `http://localhost:8080/api/post/getPostDTOByPostId/${postId}`
    );
  }

  updatePost(postId: number, formData: FormData): Observable<Post> {
    return this._httpClient.put<Post>(
      `http://localhost:8080/api/post/editPost/${postId}`,
      formData
    );
  }

  getFeedPage(page: number = 0): Observable<any> {
    return this._httpClient.get<any>(
      `http://localhost:8080/api/post/feed?page=${page}&size=15`
    );
  }

  deletePost(postId: number): Observable<any> {
    return this._httpClient.delete(
      `http://localhost:8080/api/post/deletePostById/${postId}`
    );
  }

  searchPost(keyword: string): Observable<Post[]> {
    return this._httpClient.get<Post[]>(`http://localhost:8080/api/post/search?keyword=${keyword}`);
  }

   searchInMyPosts(keyword: string): Observable<Post[]> {
    return this._httpClient.get<Post[]>(`http://localhost:8080/api/post/searchInMyPosts?keyword=${keyword}`);
  }

  getAiCompletion(text: string): Observable<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    const body = { text: text };

    return this._httpClient
      .post<CompletionResponse>(
        `http://localhost:8080/api/completion/complete`,
        body,
        {
          headers,
        }
      )
      .pipe(
        map((response) => {
          if (response.error) {
            throw new Error(response.error);
          }
          return response.completion || '';
        })
      );
  }
}

interface CompletionResponse {
  completion: string | null;
  error: string | null;
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import Topic from '../Model/topic.model';

@Injectable({
  providedIn: 'root',
})
export class TopicService {
  constructor(private _httpClient: HttpClient) {}

  getTopicsList(): Observable<Topic[]> {
    return this._httpClient.get<Topic[]>(
      'http://localhost:8080/api/topic/getTopics'
    );
  }
}

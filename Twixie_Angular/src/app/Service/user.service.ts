import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Users, { userBasicInfo, UsersLogIn } from '../Model/users.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private _httpClient: HttpClient) {}

  signUp(formData: FormData): Observable<Users> {
    return this._httpClient.post<Users>(
      'http://localhost:8080/api/users/signUp',
      formData
    );
  }

  sendVerificationEmail(email: string): Observable<any> {
    return this._httpClient.post<any>(
      `http://localhost:8080/api/users/sendVerificationEmail`,
      email
    );
  }

  logIn(user: UsersLogIn): Observable<any> {
    const httpOptions = {
      responseType: 'text' as 'json',
    };
    console.log('log in');
    return this._httpClient.post<any>(
      'http://localhost:8080/api/users/logIn',
      user,
      httpOptions
    );
  }

  signOut(): Observable<any> {
    return this._httpClient.post<any>(
      'http://localhost:8080/api/users/signout',
      null
    );
  }

  getUserDetails(): Observable<Users> {
    return this._httpClient.get<Users>(
      'http://localhost:8080/api/users/getUserDetails'
    );
  }

  getAllUserNames(): Observable<string[]> {
    return this._httpClient.get<string[]>(
      'http://localhost:8080/api/users/getUsersName'
    );
  }

  getBasicInfoCurrentUser(): Observable<userBasicInfo> {
    return this._httpClient.get<userBasicInfo>(
      `http://localhost:8080/api/users/userBasicInfo`
    );
  }
}



import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  email: string;
  password: string;
  type: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private baseUrl = 'http://localhost:8089/users/api/users';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<User> {
    const body = { email, password };
    return this.http.post<User>(`${this.baseUrl}/byCredentials`, body);
  }

  createUser(email: string, password: string): Observable<void> {
    const type = "user";
    const body = { email, password, type };
    return this.http.post<void>(`${this.baseUrl}/createUser`, body);
  }
}

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    return this.http.post<any>('https://localhost:8443/user/login', { username, password }, httpOptions);
  }

  getUser(): Observable<User[]> {
    return this.http.get<User[]>('products', {
      headers: {},
    });
  }
}
import { Injectable } from '@angular/core';
import { User } from "../model/user";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Click } from '../model/click';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl ='https://localhost:8443/user';
  private baseUrl = 'https://localhost:8443';

  constructor(private http: HttpClient) { }

  getUsers(): Observable<User[]> {
    const apiUrl = `${this.usersUrl}/all`;
    return this.http.get<User[]>(apiUrl);
  }

  getSomeUsers(page: number, size: number): Observable<any> {
    const apiUrl = `${this.usersUrl}/all&page=${page}&size=${size}`;
    return this.http.get<any>(apiUrl);
  }

  getUserById(userId: number): Observable<User> {
    const apiUrl = `${this.usersUrl}/find/${userId}`;
    return this.http.get<User>(apiUrl);
  }

  approveUser(user: User) {
    const userId = user.id; 
    const apiUrl = `${this.usersUrl}/approve/${userId}`;
  
    this.http.put(apiUrl, {}).subscribe(
      (response) => {
        console.log('User approved:', response);
      },
      (error) => {
        console.error('Error approving user:', error);
      }
    );
  }

  updateUser(userId: number, updatedUser: Partial<User>): Observable<User> {
    const url = `${this.usersUrl}/update/${userId}`;
    return this.http.put<User>(url, updatedUser);
  }

  addClick(click: Click): Observable<Click> {
    const url = `${this.baseUrl}/click/add`;
    return this.http.post<Click>(url, click);
  }

  getReservations(): Observable<any> {
    const url = `${this.baseUrl}/reservations/all`;
    return this.http.get<any>(url);
  }

  getReservationsByUser(userId: number): Observable<any> {
    const url = `${this.baseUrl}/reservations/user/${userId}`;
    return this.http.get<any>(url);
  }

}

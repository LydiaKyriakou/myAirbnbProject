import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ApiService } from './api.service';
import { User } from '../model/user';
import jwt_decode from 'jwt-decode';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  public readonly TOKEN_NAME = 'auth';
  isLoggedIn$ = this._isLoggedIn$.asObservable();
  user: User | null;

  get token(): any {
    return localStorage.getItem(this.TOKEN_NAME);
  }

  constructor(private apiService: ApiService, private router: Router) {
    this._isLoggedIn$.next(!!this.token);
    this.user = this.getUser(this.token);
  }

  login(username: string, password: string) {
    return this.apiService.login(username, password).pipe(
      tap((response: any) => {
        this._isLoggedIn$.next(true);
        localStorage.setItem(this.TOKEN_NAME, response.token);
        this.user = this.getUser(response.token);
      })
    );
  }

  logout() {
    // Remove the token from local storage
    localStorage.removeItem(this.TOKEN_NAME);
    
    // Update isLoggedIn$ to false
    this._isLoggedIn$.next(false);
    
    // Clear the user data
    this.user = null;

    this.router.navigate(['']);
  }

  private getUser(token: string): User | null {
    if (!token) {
      return null
    }
    return JSON.parse(atob(token.split('.')[1])) as User;
  }

  getDecodedAccessToken(): any {
    try {
        const token = this.token
        return jwt_decode(token);
    } catch(Error) {
        return null;
    }
  }
}
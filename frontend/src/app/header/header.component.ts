import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { User } from '../model/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  loggedInUserName: string | null = null;
  user: User | undefined;

  decodedToken: any;

  constructor(private authService: AuthService, 
              private userService: UserService
  ) { }

  logout() {
    this.authService.logout();
    this.user = undefined;
  }

  hasUserRole(): boolean {
    // Check if the user has the 'ROLE_USER' role
    return this.user?.roles?.some(role => role.name === 'ROLE_USER') ?? false;
  }

  hasHostRole(): boolean {
    // Check if the user has the 'ROLE_USER' role
    return this.user?.roles?.some(role => role.name === 'ROLE_HOST') ?? false;
  }

  hasAdminRole(): boolean {
    // Check if the user has the 'ROLE_USER' role
    return this.user?.roles?.some(role => role.name === 'ROLE_ADMIN') ?? false;
  }

  ngOnInit(): void {

    this.decodedToken = this.authService.getDecodedAccessToken();
    
    if (this.decodedToken) { 
      this.userService.getUserById(this.decodedToken.userId).subscribe(user => {
        this.user = user;
      });
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { UserService } from '../services/user.service';
import { User } from '../model/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | undefined;
  decodedToken: any;

  constructor(private authService: AuthService, private userService: UserService) {}

  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();

    if (this.decodedToken && this.decodedToken.userId) {
      this.userService.getUserById(this.decodedToken.userId).subscribe(user => {
        this.user = user;
      });
    }
  }
}

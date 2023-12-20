import { Component, OnInit } from '@angular/core';
import { User } from "../../model/user";
import { ActivatedRoute } from "@angular/router";
import { UserService } from "../../services/user.service";
import { AuthService } from 'src/app/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {
  user: User | undefined;
  decodedToken: any;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getUser();
  }
  
  getUserRole(user: User): string {
    const userRole = user.roles?.[0]?.name;
    return userRole ? userRole : 'N/A'; // Return 'N/A' if the role is not available
  }

  getUser(): void {
    const userId = this.route.snapshot.paramMap.get('id'); 
    if (userId) {
      const id = +userId; // Convert to a number
      this.userService.getUserById(id).subscribe(user => this.user = user);
    }

    this.decodedToken = this.authService.getDecodedAccessToken();

    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/access-denied']);
    }
  }

}

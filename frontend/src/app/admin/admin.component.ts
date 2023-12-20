import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { UserService } from "../services/user.service";
import { User } from "../model/user";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  users: User[] = [];

  decodedToken: any;

  currentPage = 0;
  itemsPerPage = 10;
  totalPages = 0;
  numPages = 0;

  constructor(private service: UserService, 
              private authService: AuthService, 
              private router: Router
  ) { }

  
  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
  
    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/access-denied']);
    } else {
      this.loadUsers(this.currentPage);
    }
  }


  approveUser(user: User) {
    this.service.approveUser(user);
  }


  getUserRole(user: User): string {
    const userRole = user.roles?.[0]?.name;
    return userRole ? userRole : 'N/A'; 
  }

  
  loadUsers(page: number) {
    const size = this.itemsPerPage;    
    
    this.service.getSomeUsers(page, size).subscribe(response => {
      this.users = response.content;
      this.numPages = response.totalPages;
    });
  }
  
  
  showNextRentals() {
    if ((this.currentPage + 1) < this.numPages) {
      this.currentPage++;
      this.loadUsers(this.currentPage);
    }
  }
 
  
  showPreviousRentals() {
    if ((this.currentPage + 1) > 1) {
      this.currentPage--;
      this.loadUsers(this.currentPage);
    }
  }
  

  goToUserDetails(userId: number) {
    this.router.navigate(['/user-details', userId]);
  }

}



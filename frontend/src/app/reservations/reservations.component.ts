import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';


@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {
  decodedToken: any;
  userId?: number;
  reservations?: any;

  constructor( private authService: AuthService, 
              private router: Router,
              private service: UserService
  ) { }

  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
  
    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_USER')) {
      this.router.navigate(['/access-denied']);
    } else {
      this.userId =  this.decodedToken.userId;

      if (this.userId) {
        this.service.getReservationsByUser(this.userId).subscribe(response => {
          this.reservations = response;
        });
      }
    }
  }

  makeReview(rentalId: number) {
    this.decodedToken = this.authService.getDecodedAccessToken();

    if (this.decodedToken == null) {
      alert('You have to log in first to make a review.');
    }
    else {     
      this.router.navigate(['/make-review', rentalId]);
    }
  }
  
  seeRental(rentalId: number) {
    if (rentalId != null) {
      this.router.navigate(['/rentals', rentalId], {
        queryParams: null
      });
    }
  }
}

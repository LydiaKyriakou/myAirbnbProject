import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RentaldService } from '../services/rental.service';
import { Rental } from '../model/rental';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-host',
  templateUrl: './host.component.html',
  styleUrls: ['./host.component.css']
})
export class HostComponent implements OnInit{
  rentals: Rental[] = [];

  queryParams?: number

  decodedToken: any;

  constructor( private authService: AuthService, 
              private router: Router, 
              private service: RentaldService, 
              private http: HttpClient,
  ) { }

  deleteRental(rental: Rental) {
    this.service.deleteRental(rental);
  }

  navigateToRenatlEdit(rentalId: number | null) {
    if (rentalId != null) {
      this.router.navigate(['/edit-rental', rentalId], {
        queryParams: { rentalId: rentalId }
      });
    }
  }


  ngOnInit() {

    this.decodedToken = this.authService.getDecodedAccessToken();

    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_HOST')) {
      this.router.navigate(['/access-denied']);
    }

    this.service.getRentalsByHost(this.decodedToken.userId).subscribe(rentals => this.rentals = rentals);
    this.service.getRentalsByHost(this.decodedToken.userId).subscribe(rentals => {
      this.rentals = rentals;
    
      // Assuming rentals is an array of rental objects
      for (const rental of this.rentals) {
        this.service.getAllReviews(rental.id || 0).subscribe(reviews => {
          rental.reviewSum = reviews.length;
        });
      }
    });
    
  }

}

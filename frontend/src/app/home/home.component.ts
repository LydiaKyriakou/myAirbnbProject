import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { Rental } from '../model/rental';
import { RentaldService } from '../services/rental.service';
import { Observable, forkJoin, of } from 'rxjs';
import { Click } from '../model/click';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],})
export class HomeComponent implements OnInit {
  numGuests: number = 1;
  destination: string = '';
  arrive?: Date;
  leave?: Date;

  currentPage = 0;
  itemsPerPage = 10;

  rentals: Rental[] = [];

  decodedToken: any;


  constructor(private authService: AuthService, 
              private router: Router,
              private rentalService: RentaldService,
              private userService: UserService
  ) { }


  ngOnInit() {

    this.decodedToken = this.authService.getDecodedAccessToken();

    if (this.decodedToken && this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_USER')) {
      this.router.navigate(['/access-denied']); 
    } 

    if (this.decodedToken != null) {
      this.loadRentals(this.currentPage);
    }
  }


  loadRentals(page: number) {
    const size = this.itemsPerPage;

    this.rentalService
      .getRecommendationsRentals(this.decodedToken.userId, page, size)
      .subscribe(
        (response) => {
          this.rentals = response
          console.log('Recommendations Rentals:', response);

          // Create an array of observables for fetching review sums
          const observablesSum: Observable<number>[] = this.rentals.map(rental => {
            if (rental.id !== undefined) {
              return this.rentalService.getReviewsSum(rental.id);
            } else {
              return of(0);
            }
          });

          // Create an array of observables for fetching review averages
          const observablesAvg: Observable<number>[] = this.rentals.map(rental => {
            if (rental.id !== undefined) {
              return this.rentalService.getReviewsAvg(rental.id);
            } else {
              return of(0);
            }
          });

          forkJoin(observablesSum).subscribe((reviewSums: number[]) => {
            this.rentals.forEach((rental, index) => {
              rental.reviewSum = reviewSums[index];
            });
          });

          forkJoin(observablesAvg).subscribe((reviewAvg: number[]) => {
            this.rentals.forEach((rental, index) => {
              rental.reviewAvg = reviewAvg[index];
            });
          });
        },
        (error) => {
          console.error('Error fetching recommendations rentals:', error);
        }
      );
  }


  getStarRating(avgReview: number): string {
    const maxStars = 5;
    const filledStars = Math.round(avgReview);
  
    let stars = '';
    for (let i = 0; i < maxStars; i++) {
      if (i < filledStars) {
        stars += '★'; 
      } else {
        stars += '☆'; 
      }
    }
  
    return stars;
  }

  navigateToRenatlDetails(rentalId: number | null) {
    const decodedToken = this.authService.getDecodedAccessToken();

    if (decodedToken != null && rentalId) {
      const click: Click = new Click();
      
      click.guest_id = decodedToken.userId;
      click.rental_id = rentalId;
  
      // Call the addClick function from UserService
      this.userService.addClick(click).subscribe(
        (response) => {
          // Handle the success response here
          console.log('Click added:', response);
        },
        (error) => {
          // Handle any errors that may occur during the POST request
          console.error('Error adding click:', error);
        }
      );
    }

    if (rentalId != null) {
      this.router.navigate(['/rentals', rentalId], {
        queryParams: null
      });
    }
  }

  
  navigateToSolutionsPage() {
    if (this.isFormValid(this.destination, this.numGuests, this.arrive || new Date(), this.leave || new Date())) {
      this.router.navigate(['/rentals'], {
        queryParams: {
          address: this.destination,
          guests: this.numGuests,
          checkinDate: this.arrive,
          checkoutDate: this.leave
        }
      });
    } else {
      alert("Please fill out the form correctly.");
    }
  }


  isFormValid(destination: string, numGuests: number, arrive: Date, leave: Date): boolean {
    return !!(
      destination != '' &&
      arrive && leave &&
      (arrive < leave )&&
      numGuests > 0
    );
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RentaldService } from '../services/rental.service';
import { Rental } from '../model/rental';
import { Review } from '../model/review';
import { Observable, forkJoin, of } from 'rxjs';
import { Router } from '@angular/router';
import { Click } from '../model/click';
import { UserService } from '../services/user.service';
import { AuthService } from '../auth/auth.service';


@Component({
  selector: 'app-rentals',
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.css']
})
export class RentalsComponent implements OnInit {
  queryParams: any = {};
  totalDays: number = 0;

  rental?: Rental;
  destination?: string; 
  numGuests?: number; 
  arrive?: Date; 
  leave?: Date;

  rentals: Rental[] = [];
  validRentals: Rental[] = [];
  reviews: Review [] = [];
  rentalReviews: { [rentalId: number]: Review[] } = {};

  displayedRentals: Rental[] = [];
  currentPage = 0;
  itemsPerPage = 10;
  totalPages = 0;
  numPages = 0;

  type: string = '';
  maxCost: number = 0;
  wifi: boolean = false;
  ac: boolean = false;
  kitchen: boolean = false;
  tv: boolean = false;
  parking: boolean = false;
  elevator: boolean = false;
  filters: any = null;

  newFilters: boolean = false;

  constructor(private rentalService: RentaldService, 
              private route: ActivatedRoute, 
              private router: Router,
              private userService: UserService,
              private authService: AuthService
  ) { }

  
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.queryParams = params;

      const guests = params['guests'];
      const address = params['address'];
      const checkinDate = params['checkinDate'];
      const checkoutDate = params['checkoutDate'];

      this.destination = address; 
      this.numGuests = guests; 
      this.arrive = checkinDate; 
      this.leave = checkoutDate;

      if (this.newFilters == false) {
        this.loadRentals(this.currentPage, address, checkinDate, checkoutDate, guests, this.filters, this.newFilters);
      }
      
    });
  }


  showNextRentals() {
    this.currentPage++;
    this.loadRentals( this.currentPage, 
                      this.queryParams.address, 
                      this.queryParams.checkinDate, 
                      this.queryParams.checkoutDate, 
                      this.queryParams.guests,
                      this.filters,
                      this.newFilters);
  }


  showPreviousRentals() {
    this.currentPage--;
    this.loadRentals( this.currentPage, 
                      this.queryParams.address, 
                      this.queryParams.checkinDate, 
                      this.queryParams.checkoutDate, 
                      this.queryParams.guests,
                      this.filters,
                      this.newFilters);
  }


  loadRentals(page: number, city: string, checkin: Date, checkout: Date, guests: number, filters: any, newFilters: boolean) {
    const size = this.itemsPerPage;

    if (filters == null) {
  
      this.rentalService.getSomeRentals(city, checkin, checkout, guests, page, size).subscribe((rentalsResponse: any) => {
        this.rentals = rentalsResponse.content;

        this.numPages = rentalsResponse.totalPages;
  
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
      });
    }
    else {
      if (this.newFilters == true) {
        this.currentPage = 0;
        this.newFilters = false;
      }

      this.rentalService.getSomeRentalsFilters(page, size).subscribe((rentalsResponse: any) => {
        this.rentals = rentalsResponse.content;
        this.numPages = rentalsResponse.totalPages;
  
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
      });

    }

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


  applyFilters() {
    this.route.queryParams.subscribe(params => {
      const guests = params['guests'];
      const address = params['address'];
      const checkinDate = params['checkinDate'];
      const checkoutDate = params['checkoutDate'];

      if (this.type != '') {
        this.filters = {
          guests: guests,
          checkin: checkinDate,
          checkout: checkoutDate,
          city: address,
          wifi: this.wifi,
          ac: this.ac,
          kitchen: this.kitchen,
          tv: this.tv,
          parking: this.parking,
          elevator: this.elevator,
          maxPrice: this.maxCost,
          type: this.type
        }
      }
      else {
        this.filters = {
          guests: guests,
          checkin: checkinDate,
          checkout: checkoutDate,
          city: address,
          wifi: this.wifi,
          ac: this.ac,
          kitchen: this.kitchen,
          tv: this.tv,
          parking: this.parking,
          elevator: this.elevator,
          maxPrice: this.maxCost
        }
      }

      this.newFilters = true;

      this.rentalService.postFilters(this.filters).subscribe(
        (response) => {
          console.log('Filter results added successfully:', response);
          this.loadRentals( this.currentPage, 
                            this.queryParams.address, 
                            this.queryParams.checkinDate, 
                            this.queryParams.checkoutDate, 
                            this.queryParams.guests,
                            this.filters,
                            this.newFilters);
        },
        (error) => {
          console.error('Error adding filter results:', error);
        }
      );

    });

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
        queryParams: this.queryParams
      });
    }
  }
  
}


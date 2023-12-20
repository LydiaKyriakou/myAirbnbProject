import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RentaldService } from 'src/app/services/rental.service';
import { Rental } from 'src/app/model/rental';
import { Review } from 'src/app/model/review';
import { AuthService } from 'src/app/auth/auth.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/model/user';
import * as L from 'leaflet';
import 'leaflet-control-geocoder';


@Component({
  selector: 'app-rental-details',
  templateUrl: './rental-details.component.html',
  styleUrls: ['./rental-details.component.css']
})
export class RentalDetailsComponent implements OnInit {
  rental: Rental | undefined;
  reviews: Review [] = [];
  availableDates: Date[] = [];

  hostData: User | undefined;

  queryParams: any = {};
  form?: boolean;

  decodedToken: any;
  userId?: number;
  hostId?: number;
  guests?: number;

  startDate?: Date;
  endDate?: Date;

  numGuests: number = 1;
  arrive?: Date;
  leave?: Date;

  constructor(
    private route: ActivatedRoute,
    private rentalService: RentaldService,
    private authService: AuthService,
    private router: Router,
    private userService: UserService
  ) {}
    
  private map: L.Map | null = null;

  private initMap(coordinatex: number, coordinatey: number): void {
    this.map = L.map('map', {
      center: [coordinatex, coordinatey], 
      zoom: 15 
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 10,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    const customIcon = L.icon({
      iconUrl: '../../assets/images/placeholder.png',
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32] 
    });

    tiles.addTo(this.map);

    // Add a single marker at the specified coordinates
    L.marker([coordinatex, coordinatey], { icon: customIcon }).addTo(this.map);
  }

  loadReviews(rentalId: number): void {
    this.rentalService.getAllReviews(rentalId).subscribe((reviews: Review[]) => {
      this.reviews = reviews;
    });
  }

  sentMessage() {
    if (this.decodedToken == null) {
      alert('You have to log in first to sent a message to the host.');
    }
    else {
      this.router.navigate(['/send-message', this.hostId, this.userId]);
    }
  }

  ngOnInit(): void {
    this.decodedToken = this.authService.getDecodedAccessToken();

    if (this.decodedToken != null) {
      this.userId = this.decodedToken.userId;
    }

    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.route.queryParams.subscribe(params => {
      this.queryParams = params;

      if (Object.keys(params).length === 0) {
        this.form = false;
      }
      else {
        this.form = true;
      }

      const checkinDateStr = params['checkinDate'];
      const checkoutDateStr = params['checkoutDate'];
      this.guests = params['guests'];

      if (checkinDateStr && checkoutDateStr) {
        this.startDate = new Date(checkinDateStr);
        this.endDate = new Date(checkoutDateStr);
      }  
        

      this.rentalService.getById(id).subscribe((rental) => {
        this.rental = rental;
        
        if (this.rental?.coordinatex !== undefined && this.rental?.coordinatey !== undefined) {
          this.initMap(this.rental.coordinatex, this.rental.coordinatey);
        }
          
        if (this.rental?.availableDates) {
          this.availableDates = this.rental.availableDates;
        }

        this.hostId = this.rental.host;

        if (this.hostId) {
          this.userService.getUserById(this.hostId).subscribe((host) => {
            this.hostData = host;
          });
        }
        
        this.loadReviews(id);
      });
    });
  }


  makeReservation(rentalId: number) {
    this.decodedToken = this.authService.getDecodedAccessToken();

    if (this.decodedToken == null) {
      alert('You have to log in first to make a reservation.');
    }
    else {     

      const reservation = {
        guest: {
          id: this.decodedToken.userId
        },
        rental: {
          id: rentalId
        },
        startDate: this.startDate,
        endDate: this.endDate
      };


      this.rentalService.makeReservation(reservation).subscribe(
        (response) => {
          console.log('Reservation successful:', response);
          alert('Your reservation has been successfully registered.');
          this.router.navigate(['']);
        },
        (error) => {
          console.error('Error making reservation:', error);
          if (error.status === 500) {
            // Handle status 500 error here
            alert('This rental is not available those dates.');
          } else {
            // Handle other errors here
            alert('An error occurred. Please try again.');
          }
        }
      );

    }
  }


  makeNewReservation(rentalId: number | null) {    
    if (this.rental?.maxVisitors && this.arrive && this.leave) {
      if (this.numGuests <= this.rental?.maxVisitors && this.arrive <= this.leave) {
        const reservation = {
          guest: {
            id: this.decodedToken.userId
          },
          rental: {
            id: rentalId
          },
          startDate: this.arrive,
          endDate: this.leave
        };
    
    
        console.log('Reservation successfulreservation:', reservation);

        this.rentalService.makeReservation(reservation).subscribe(
          (response) => {
            console.log('Reservation successful:', response);
            alert('Your reservation has been successfully registered.');
            this.router.navigate(['']);
          },
          (error) => {
            console.error('Error making reservation:', error);
            if (error.status === 500) {
              // Handle status 500 error here
              alert('This rental is not available those dates.');
            } else {
              // Handle other errors here
              alert('An error occurred. Please try again.');
            }
          }
        );
      }
      else if (this.rental?.maxVisitors) {
        alert('We are sorry, but the guests are more than the max visitors for this rental.');
      }
    }
  }

  daysBetweenDates(startDate: Date | undefined, endDate: Date | undefined): number | string {
    if (!startDate || !endDate) {
      return 'N/A';
    }
  
    const timeDifference = endDate.getTime() - startDate.getTime();
    const daysDifference = Math.ceil(timeDifference / (1000 * 60 * 60 * 24));

    return daysDifference; 
  }

  calculatePricePerDay(): number | string {
  
    const days = this.daysBetweenDates(this.startDate, this.endDate);

    if (typeof days === 'number') {
      const basePrice = this.rental?.minPrice || 0;
      const pricePerPerson = this.rental?.plusPricePerPerson || 0;
      const guests = this.queryParams.guests || 0;
  
      return (basePrice + pricePerPerson * (guests - 1)) * days;
    } else {
      return 'N/A';
    }
  }
  

  getStarRating(review: number): string {
    const maxStars = 5;
    const filledStars = Math.round(review);
  
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
  
}

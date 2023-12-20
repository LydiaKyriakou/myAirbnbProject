import { Component, OnInit } from '@angular/core';
import { RentaldService } from 'src/app/services/rental.service';
import { Review } from 'src/app/model/review';
import { AuthService } from 'src/app/auth/auth.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-make-review',
  templateUrl: './make-review.component.html',
  styleUrls: ['./make-review.component.css']
})
export class MakeReviewComponent implements OnInit {
  decodedToken: any;

  username?: string;
  id: number | undefined;

  review: Review = { username: '', rental_id: 0, date: new Date(), rating: 1, review: '' };
  stars = [1, 2, 3, 4, 5];

  constructor(private rentalService: RentaldService, private authService: AuthService, private route: ActivatedRoute) {}

  submitReview() {
    const review: Review = {
      username: this.username || '', 
      rental_id: this.id || 0,
      date: new Date,
      review: this.review.review,
      rating: this.review.rating
    };

    // Call the addReviewToRental function
    this.rentalService.addReviewToRental(review).subscribe(
      (response) => {
        console.log('Review added successfully:', response);
        alert("Review added successfully.")
        window.history.back();
      },
      (error) => {
        console.error('Error adding review:', error);
        alert("Error adding review.")
      }
    );
  }

  ngOnInit() {  
    
    this.route.params.subscribe(params => {
      this.id = +params['id']; 
      
      // You can use 'id' here as needed.
      console.log('Parameter id:', this.id);
    });
    
    this.decodedToken = this.authService.getDecodedAccessToken();
  
    this.username = this.decodedToken.username
    
  }

  setRating(rating: number) {
    this.review.rating = rating;
  }
}

package com.example.newairbnb.controller;

import com.example.newairbnb.service.RentalService;
import com.example.newairbnb.service.ReviewService;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.Reservation;
import com.example.newairbnb.user.Review;
import com.example.newairbnb.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final RentalService rentalService;

    @Autowired
    public ReviewController(ReviewService reviewService, RentalService rentalService) {
        this.reviewService = reviewService;
        this.rentalService = rentalService;
    }

    @GetMapping("/rental/{rental_id}")
    public ResponseEntity<List<Review>> getReviewsByRental(@PathVariable Long rental_id) {
        Rental rental = rentalService.findRentalById(rental_id);
        List<Review> reviews = reviewService.getReviewsByRentalId(rental_id);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/reviewsSum/rental={rental_id}")
    public ResponseEntity<Integer> getReviewsSum(@PathVariable Long rental_id) {
        Rental rental = rentalService.findRentalById(rental_id);
        List<Review> reviews = reviewService.getReviewsByRentalId(rental_id);
        return new ResponseEntity<>(reviews.size(), HttpStatus.OK);
    }

    @GetMapping("/reviewsAvg/rental={rental_id}")
    public ResponseEntity<Float> getReviewsAvg(@PathVariable Long rental_id) {
        Rental rental = rentalService.findRentalById(rental_id);
        List<Review> reviews = reviewService.getReviewsByRentalId(rental_id);
        int i = 0, sum = 0;
        float avg = 0;
        for(Review review : reviews){
            i++;
            sum += review.getRating();
        }
        if( i != 0){
            avg = sum/i;
        }
        return new ResponseEntity<>(avg, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{rentalId}/add")
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        Rental rental = rentalService.findRentalById(review.getRental_id());
        Long rentalId = review.getRental_id();
        rental.addReview(review);
        Review newReview = reviewService.addReview(review);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews () {
        List<Review> reviews = reviewService.findAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

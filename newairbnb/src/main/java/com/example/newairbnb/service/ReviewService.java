package com.example.newairbnb.service;

import com.example.newairbnb.repository.ReviewRepository;
import com.example.newairbnb.user.Review;
import com.example.newairbnb.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public List<Review> findAllReviews(){
        return reviewRepository.findAll();
    }
    public List<Review> getReviewsByRentalId(Long rental_id){
        List<Review> reviews = reviewRepository.findAll();
        List<Review> rental_reviews = new ArrayList<>();

        for (Review review : reviews) {
            if(review.getRental_id().equals(rental_id)){
                rental_reviews.add(review);
            }
        }
        return rental_reviews;
    }

    public Review addReview(Review review) {
        String username = review.getUsername();
        Optional<User> user = userService.findByUsername(username);
        review.setGuest_id(user.get().getId());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

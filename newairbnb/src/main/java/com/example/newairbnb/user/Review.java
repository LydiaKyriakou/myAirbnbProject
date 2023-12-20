package com.example.newairbnb.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String guest_username;
    private Long guest_id;

    private Long rental_id;
    private LocalDate date;
    private String review;
    private Integer rating;

    public Review(Long guest_id, Long rental_id, String guest_username, LocalDate date, String review, Integer rating) {
        this.guest_username = guest_username;
        this.guest_id = guest_id;
        this.date = date;
        this.review = review;
        this.rating = rating;
        this.rental_id = rental_id;
    }

    public Review() {
    }

    public Long getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(Long guest_id) {
        this.guest_id = guest_id;
    }

    public Long getRental_id() {
        return rental_id;
    }

    public void setRental_id(Long rental_id) {
        this.rental_id = rental_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return guest_username;
    }

    public void setUsername(String guest_username) {
        this.guest_username = guest_username;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", guest_username='" + guest_username + '\'' +
                ", guest_id=" + guest_id +
                ", rental_id=" + rental_id +
                ", date=" + date +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }
}

package com.example.newairbnb.request;

import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.User;

import java.time.LocalDate;

public class ReservationRequest {
    private User guest;
    private Rental rental;
    private LocalDate startDate;
    private LocalDate endDate;

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
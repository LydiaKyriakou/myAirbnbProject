package com.example.newairbnb.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rental_availabilities")
public class RentalAvail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "rental_id")
//    private Rental rental;
//    @Column(nullable = false, updatable = false)
    private Long rental_id;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(columnDefinition = "BOOL")
    private boolean isAvailable;

    public RentalAvail() {
    }

    public RentalAvail( Long rental, LocalDate date, boolean isAvailable) {

        this.rental_id = rental;
        this.date = date;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRental() {
        return rental_id;
    }

    public void setRental(Long rental) {
        this.rental_id = rental;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
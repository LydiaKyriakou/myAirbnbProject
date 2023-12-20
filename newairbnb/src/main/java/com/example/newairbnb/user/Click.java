package com.example.newairbnb.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long guest_id;
    private Long rental_id;

    public Click( Long guest_id, Long rental_id) {
        this.guest_id = guest_id;
        this.rental_id = rental_id;
    }

    public Click() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

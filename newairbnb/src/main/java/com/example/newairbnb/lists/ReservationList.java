package com.example.newairbnb.lists;

import com.example.newairbnb.user.Reservation;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "reservations")
public class ReservationList {

    private List<Reservation> reservations;

    @XmlElement(name = "reservation")
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
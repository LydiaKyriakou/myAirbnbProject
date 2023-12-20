package com.example.newairbnb.lists;

import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "rentals")
public class RentalList {

    private List<Rental> rentals;

    @XmlElement(name = "rental")
    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}

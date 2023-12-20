package com.example.newairbnb.user;

import javax.persistence.Column;

public class Results {
    protected Integer guests;
    protected String checkin;
    protected String checkout;
    protected String city;




    //amenities
    @Column(columnDefinition = "BOOL")
    protected Boolean wifi;
    @Column(columnDefinition = "BOOL")
    protected Boolean ac;
    @Column(columnDefinition = "BOOL")
    protected Boolean kitchen;
    @Column(columnDefinition = "BOOL")
    protected Boolean tv;
    @Column(columnDefinition = "BOOL")
    protected Boolean parking;
    @Column(columnDefinition = "BOOL")
    protected Boolean elevator;
    protected Integer maxPrice;

    protected String type;

    public Results() {
    }

    public Results(Integer guests, String checkin, String checkout, String city,
                   Boolean wifi, Boolean ac, Boolean kitchen, Boolean tv, Boolean parking,
                   Boolean elevator, Integer maxPrice, String type) {
        this.guests = guests;
        this.type = type;
        this.checkin = checkin;
        this.checkout = checkout;
        this.city = city;
        this.wifi = wifi;
        this.ac = ac;
        this.kitchen = kitchen;
        this.tv = tv;
        this.parking = parking;
        this.elevator = elevator;
        this.maxPrice = maxPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean getAc() {
        return ac;
    }

    public void setAc(Boolean ac) {
        this.ac = ac;
    }

    public Boolean getKitchen() {
        return kitchen;
    }

    public void setKitchen(Boolean kitchen) {
        this.kitchen = kitchen;
    }

    public Boolean getTv() {
        return tv;
    }

    public void setTv(Boolean tv) {
        this.tv = tv;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getElevator() {
        return elevator;
    }

    public void setElevator(Boolean elevator) {
        this.elevator = elevator;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }
}

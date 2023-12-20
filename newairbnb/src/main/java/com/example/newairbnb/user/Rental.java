package com.example.newairbnb.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    protected Long id;
    protected Integer maxVisitors;
    protected Double minPrice;
    protected Double plusPricePerPerson;

    //house characteristics
    protected Integer beds;
    protected Float bathrooms;
    protected String  type;
    protected Integer bedrooms;
    @Column(columnDefinition = "BOOL")
    protected Boolean livingroom;
    protected  Integer area;

    protected String description;

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

    //rules
    @Column(columnDefinition = "BOOL")
    protected Boolean smoking;
    @Column(columnDefinition = "BOOL")
    protected Boolean pets;
    @Column(columnDefinition = "BOOL")
    protected Boolean events;
    protected Integer minDays;

    //location
    protected String city;
    protected String address;
    protected String neighbor;
    protected String transportation;

    //openstreet map
    protected Double coordinatex;
    protected Double coordinatey;
    //import photosss
    @OneToMany(mappedBy = "rental_id", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Image> imageUrls;
    @OneToMany(mappedBy = "rental_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalAvail> rentalAvail = new ArrayList<>();

    private Long host;

    @OneToMany(mappedBy = "rental_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    public Rental(Integer maxVisitors, Double minPrice, Double plusPricePerPerson, Integer beds,
                  Float bathrooms, String type, Integer bedrooms, Boolean livingroom, Integer area,
                  String description, Boolean wifi, Boolean AC, Boolean kitchen, Boolean tv, Boolean parking,
                  Boolean elevator, Boolean smoking, Boolean pets, Boolean events, Integer minDays, String city,
                  String address, String neighbor, String transportation, Double coordinatex, Double coordinatey,
                  List<RentalAvail> rentalAvail, List<Image> imageUrls, Long host, List<Review> reviews) {
        this.maxVisitors = maxVisitors;
        this.minPrice = minPrice;
        this.plusPricePerPerson = plusPricePerPerson;
        this.beds = beds;
        this.bathrooms = bathrooms;
        this.type = type;
        this.bedrooms = bedrooms;
        this.livingroom = livingroom;
        this.area = area;
        this.description = description;
        this.wifi = wifi;
        this.ac = AC;
        this.kitchen = kitchen;
        this.tv = tv;
        this.parking = parking;
        this.elevator = elevator;
        this.smoking = smoking;
        this.pets = pets;
        this.events = events;
        this.minDays = minDays;
        this.city = city;
        this.address = address;
        this.neighbor = neighbor;
        this.transportation = transportation;
        this.coordinatex = coordinatex;
        this.coordinatey = coordinatey;
        this.rentalAvail = rentalAvail;
        this.imageUrls = imageUrls;
        this.host = host;
        this.reviews = reviews;
    }

    public Rental() {
    }

    public List<Image> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<Image> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void addImageUrls(Image imageUrl) {
        imageUrl.setRental_id(this.id);
        this.imageUrls.add(imageUrl);
    }
     // Add a method to add an available date
    public void addAvailableDate(Long rentalId , LocalDate date) {
        RentalAvail availability = new RentalAvail(rentalId, date, true);
        rentalAvail.add(availability);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        review.setRental_id(this.id);
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        review.setRental_id(null);
        this.reviews.remove(review);
    }

    public List<RentalAvail> getRentalAvail() {
        return rentalAvail;
    }

    public void setRentalAvail(List<RentalAvail> rentalAvail) {
        this.rentalAvail = rentalAvail;
    }



    public List<LocalDate> getAvailableDates() {
        return rentalAvail.stream()
                .filter(RentalAvail::isAvailable)
                .map(RentalAvail::getDate)
                .collect(Collectors.toList());
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setHost(Long host_id) {
        this.host = host_id;
    }

    public Integer getMaxVisitors() {
        return maxVisitors;
    }

    public void setMaxVisitors(Integer maxVisitors) {
        this.maxVisitors = maxVisitors;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getPlusPricePerPerson() {
        return plusPricePerPerson;
    }

    public void setPlusPricePerPerson(Double plusPricePerPerson) {
        this.plusPricePerPerson = plusPricePerPerson;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Float getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Float bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Boolean getLivingroom() {
        return livingroom;
    }

    public void setLivingroom(Boolean livingroom) {
        this.livingroom = livingroom;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSmoking() {
        return smoking;
    }

    public void setSmoking(Boolean smoking) {
        this.smoking = smoking;
    }

    public Boolean getPets() {
        return pets;
    }

    public void setPets(Boolean pets) {
        this.pets = pets;
    }

    public Boolean getEvents() {
        return events;
    }

    public void setEvents(Boolean events) {
        this.events = events;
    }

    public Integer getMinDays() {
        return minDays;
    }

    public void setMinDays(Integer minDays) {
        this.minDays = minDays;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(String neighbor) {
        this.neighbor = neighbor;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getCoordinatex() {
        return coordinatex;
    }

    public void setCoordinatex(Double coordinatex) {
        this.coordinatex = coordinatex;
    }

    public Double getCoordinatey() {
        return coordinatey;
    }

    public void setCoordinatey(Double coordinatey) {
        this.coordinatey = coordinatey;
    }

    public Long getHost() {
        return host;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", maxVisitors=" + maxVisitors +
                ", minPrice=" + minPrice +
                ", plusPricePerPerson=" + plusPricePerPerson +
                ", beds=" + beds +
                ", bathrooms=" + bathrooms +
                ", type='" + type + '\'' +
                ", bedrooms=" + bedrooms +
                ", livingroom=" + livingroom +
                ", area=" + area +
                ", description='" + description + '\'' +
                ", wifi=" + wifi +
                ", AC=" + ac +
                ", kitchen=" + kitchen +
                ", tv=" + tv +
                ", parking=" + parking +
                ", elevator=" + elevator +
                ", smoking=" + smoking +
                ", pets=" + pets +
                ", events=" + events +
                ", minDays=" + minDays +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", neighbor='" + neighbor + '\'' +
                ", transportation='" + transportation + '\'' +
                ", coordinatex=" + coordinatex +
                ", coordinatey=" + coordinatey +
                ", rentalAvail=" + rentalAvail +
                ", host=" + host +
                ", reviews=" + reviews +
                '}';
    }
}

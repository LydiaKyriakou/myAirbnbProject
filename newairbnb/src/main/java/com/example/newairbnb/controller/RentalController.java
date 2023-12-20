package com.example.newairbnb.controller;

import com.example.newairbnb.service.ImageService;
import com.example.newairbnb.service.RentalAvailService;
import com.example.newairbnb.service.RentalService;
import com.example.newairbnb.service.UserService;
import com.example.newairbnb.user.Image;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.RentalAvail;
import com.example.newairbnb.user.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/rental")
public class RentalController {
    private final RentalService rentalService;
    private final UserService userService;
    private final ImageService imageService;
    private final RentalAvailService rentalAvailService;

    public RentalController(RentalService rentalService, UserService userService, ImageService imageService, RentalAvailService rentalAvailService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.imageService = imageService;
        this.rentalAvailService = rentalAvailService;
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/host/{host_id}")
    public ResponseEntity<List<Rental>> getRentalsByhHostId(@PathVariable Long host_id) {
        User user = userService.findUserById(host_id);
        List<Rental> rentals = rentalService.getRentalsByhHostId(host_id);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Rental>> getAllRentals () {
        List<Rental> rentals = rentalService.findAllRentals();
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Rental> getRentalById (@PathVariable("id") Long id) {
        Rental rental = rentalService.findRentalById(id);
        return new ResponseEntity<>(rental, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HOST')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Rental> updateRental(@PathVariable("id") Long id,@RequestBody Rental updatedRental ) {
        Optional<Rental> optionalRental = rentalService.getRental(id);

        if (optionalRental.isPresent()) {
            Rental existingRental = optionalRental.get();

            if (updatedRental.getMaxVisitors() != null) {
                existingRental.setMaxVisitors(updatedRental.getMaxVisitors());
            }

            if (updatedRental.getMinPrice() != null) {
                existingRental.setMinPrice(updatedRental.getMinPrice());
            }

            if (updatedRental.getPlusPricePerPerson() != null) {
                existingRental.setPlusPricePerPerson(updatedRental.getPlusPricePerPerson());
            }

            if (updatedRental.getBeds() != null) {
                existingRental.setBeds(updatedRental.getBeds());
            }

            if (updatedRental.getBathrooms() != null) {
                existingRental.setBathrooms(updatedRental.getBathrooms());
            }

            if (updatedRental.getType() != null) {
                existingRental.setType(updatedRental.getType());
            }
            List<Image> images = updatedRental.getImageUrls();
            if (images != null) {
                for(Image image : images){
                    existingRental.addImageUrls(image);
                    imageService.addImage(image);
                    imageService.updateNullRentalIdsToNewRental(existingRental.getId());
                }
            }
            List<RentalAvail> rentalAvails = updatedRental.getRentalAvail();
            if (rentalAvails != null) {
                for(RentalAvail rentalAvail : rentalAvails){
                    //existingRental.addAvailableDate(rentalAvail.);
                    rentalAvailService.addAvailDate(rentalAvail);
                    rentalAvailService.updateNullRentalIdsToNewRental(existingRental.getId());
                }
            }

            if (updatedRental.getBedrooms() != null) {
                existingRental.setBedrooms(updatedRental.getBedrooms());
            }

            if (updatedRental.getLivingroom() != null) {
                existingRental.setLivingroom(updatedRental.getLivingroom());
            }

            if (updatedRental.getDescription() != null) {
                existingRental.setDescription(updatedRental.getDescription());
            }

            if (updatedRental.getBeds() != null) {
                existingRental.setBeds(updatedRental.getBeds());
            }

            if (updatedRental.getSmoking() != null) {
                existingRental.setSmoking(updatedRental.getSmoking());
            }

            if (updatedRental.getPets() != null) {
                existingRental.setPets(updatedRental.getPets());
            }


            if (updatedRental.getEvents() != null) {
                existingRental.setEvents(updatedRental.getEvents());
            }

            if (updatedRental.getMinDays() != null) {
                existingRental.setMinDays(updatedRental.getMinDays());
            }

            if (updatedRental.getAddress() != null) {
                existingRental.setAddress(updatedRental.getAddress());
            }

            if (updatedRental.getNeighbor() != null) {
                existingRental.setNeighbor(updatedRental.getNeighbor());
            }

            if (updatedRental.getWifi() != null) {
                existingRental.setWifi(updatedRental.getWifi());
            }

            if (updatedRental.getAc() != null) {
                existingRental.setAc(updatedRental.getAc());
            }

            if (updatedRental.getKitchen() != null) {
                existingRental.setKitchen(updatedRental.getKitchen());
            }

            if (updatedRental.getTv() != null) {
                existingRental.setTv(updatedRental.getTv());
            }

            if (updatedRental.getParking() != null) {
                existingRental.setParking(updatedRental.getParking());
            }

            if (updatedRental.getElevator() != null) {
                existingRental.setElevator(updatedRental.getElevator());
            }

            if (updatedRental.getCity() != null) {
                existingRental.setCity(updatedRental.getCity());
            }

            if (updatedRental.getCoordinatex() != null) {
                existingRental.setCoordinatex(updatedRental.getCoordinatex());
            }

            if (updatedRental.getCoordinatey() != null) {
                existingRental.setCoordinatey(updatedRental.getCoordinatey());
            }
            if(updatedRental.getType() != null){
                existingRental.setType(updatedRental.getType());
            }

            Rental savedRental = rentalService.updateRental(id,existingRental);

            return new ResponseEntity<>(savedRental, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('HOST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRental(@PathVariable("id") Long id) {
        rentalService.deleteRental(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
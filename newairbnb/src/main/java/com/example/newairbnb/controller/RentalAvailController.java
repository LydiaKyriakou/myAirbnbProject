package com.example.newairbnb.controller;

import com.example.newairbnb.service.ImageService;
import com.example.newairbnb.service.RentalAvailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentaldates")
public class RentalAvailController {

    private final RentalAvailService rentalAvailService;

    public RentalAvailController(RentalAvailService rentalAvailService) {
        this.rentalAvailService = rentalAvailService;
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRentalAvail(@PathVariable("id") Long id) {
        rentalAvailService.deleteRentalAvail(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

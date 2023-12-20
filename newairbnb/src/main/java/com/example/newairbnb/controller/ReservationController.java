package com.example.newairbnb.controller;

import com.example.newairbnb.request.ReservationRequest;
import com.example.newairbnb.service.RentalService;
import com.example.newairbnb.service.ReservationService;
import com.example.newairbnb.service.UserService;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.Reservation;
import com.example.newairbnb.user.Review;
import com.example.newairbnb.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;

    @Autowired
    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations () {
        List<Reservation> reservations = reservationService.findAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequest request) {
        User guest = request.getGuest();
        Rental rental = request.getRental();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        Reservation reservation = reservationService.addReservation(guest, rental, startDate, endDate);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long user_id) {
        User user = userService.findUserById(user_id);
        List<Reservation> reservations = reservationService.getReservationsByUser(user_id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

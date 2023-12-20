package com.example.newairbnb.service;

import com.example.newairbnb.exception.UserNotFoundException;
import com.example.newairbnb.repository.ReservationRepository;
import com.example.newairbnb.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RentalService rentalService;
    private final UserService userService;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RentalService rentalService, UserService userService, RentalAvailService rentalAvailService) {
        this.reservationRepository = reservationRepository;
        this.rentalService = rentalService;
        this.userService = userService;

    }

    public List<Reservation> getReservationsByUser(Long user_id){
        List<Reservation> reservations = this.findAllReservations();
        List<Reservation> rental_reservations= new ArrayList<>();

        for (Reservation reservation : reservations) {
            Long guest_id = reservation.getGuest().getId();
            if(guest_id.equals(user_id)){
                rental_reservations.add(reservation);
            }
        }
        return rental_reservations;
    }
    public List<Reservation> findAllReservations(){
        return reservationRepository.findAll();
    }

    public Reservation addReservation(User guest, Rental rental, LocalDate startDate, LocalDate endDate) {

        if (rentalService.isRentalAvailable(rental, startDate, endDate)) {

            Reservation reservation = new Reservation(guest, rental, startDate, endDate);
            Rental availRental = rentalService.findRentalById(rental.getId());
            List<RentalAvail> rentalAvail = availRental.getRentalAvail();
            System.out.println("before for" + availRental.getId());
            for (RentalAvail checkAvail : rentalAvail){
                if(checkAvail.getDate().isEqual(startDate) || checkAvail.getDate().isEqual(endDate) || (checkAvail.getDate().isAfter(startDate) && checkAvail.getDate().isBefore(endDate))){
                    checkAvail.setAvailable(false);
                    System.out.println("hey");

                }
            }

            return reservationRepository.save(reservation);
        } else {

            throw new UserNotFoundException("Rental is not available for the requested dates");
        }
    }

    public void deleteReservation(@PathVariable Long id) {
        reservationRepository.deleteById(id);
    }


}

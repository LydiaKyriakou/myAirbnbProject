package com.example.newairbnb.service;

import com.example.newairbnb.exception.UserNotFoundException;
import com.example.newairbnb.repository.RentalRepository;
import com.example.newairbnb.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental addRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public List<Rental> findAllRentals(){
        return rentalRepository.findAll();
    }

    public Page<Rental> getRentalsByRentalIds(List<Long> rentalIds, Pageable pageable) {
        return rentalRepository.findAllByIdIn(rentalIds, pageable);
    }

    public Optional<Rental> getRental(@PathVariable Long id) {
        return rentalRepository.findById(id);
    }

    public Rental updateRental(Long rentalId, Rental updatedRental) {


        return rentalRepository.save(updatedRental);
    }


    public void deleteRental(@PathVariable Long id) {
        rentalRepository.deleteById(id);
    }

    public Rental findRentalById(Long id) {
        return rentalRepository.findRentalById(id)
                .orElseThrow(() -> new UserNotFoundException("Rental by id " + id + " was not found"));
    }

    public List<Long> findAllRentalIds() {
        // Query the database to fetch user IDs of users with ROLE_USER
        List<Long> userIds = rentalRepository.findAllRentalIds();

        return userIds;
    }

    public  Page<Rental>  findAvailableRentalsBetweenDates(String startDate, String endDate, String city, Integer guests, Pageable pageable){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long days = -1L;
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);

            // Calculate the difference in days
            long differenceInMilliseconds = date2.getTime() - date1.getTime();
            days = TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS) + 1;
        } catch (ParseException e) {
            // Handle the exception here
            e.printStackTrace();
        }
        Page<Rental> rentals =  rentalRepository.findAvailableRentalsBetweenDates(startDate,endDate,city,guests,days,pageable);
        return rentals;
    }

    public  Page<Rental>  sendResults(Results results, Pageable pageable){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long days = -1L;
        try {
            Date date1 = sdf.parse(results.getCheckin());
            Date date2 = sdf.parse(results.getCheckout());

            // Calculate the difference in days
            long differenceInMilliseconds = date2.getTime() - date1.getTime();
            days = TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS) + 1;
        } catch (ParseException e) {
            // Handle the exception here
            e.printStackTrace();
        }

        Page<Rental> rentals =  rentalRepository.sendResults(results.getGuests(), results.getCheckin(),
                results.getCheckout(),results.getCity(),results.getWifi(),results.getAc(),results.getKitchen(),
                results.getTv(),results.getParking(),results.getElevator(),results.getMaxPrice(),days,results.getType(),pageable);

        return rentals;
    }

    public List<Rental> getRentalsByhHostId(Long host_id){
        List<Rental> rentals = rentalRepository.findAll();
        List<Rental> host_rentals = new ArrayList<>();

        for (Rental rental : rentals) {
            if(rental.getHost().equals(host_id)){
                host_rentals.add(rental);
            }
        }
        return host_rentals;
    }


    public boolean isRentalAvailable(Rental rentalAv, LocalDate startDate, LocalDate endDate) {
        // Fetch the rental by its ID
        Optional<Rental> optionalRental = rentalRepository.findById(rentalAv.getId());


        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            List<LocalDate> availableDates = rental.getAvailableDates();
            for (LocalDate date : rental.getAvailableDates()) {
                System.out.println(date);
            }

            // Check if all dates in the requested range are available
            return availableDates.stream()
                    .filter(date -> date.isAfter(startDate.minusDays(1)))
                    .filter(date -> date.isBefore(endDate.plusDays(1)))
                    .count() == ChronoUnit.DAYS.between(startDate, endDate) + 1;
        } else {
            // Handle the case where the rental is not found
            throw new EntityNotFoundException("Rental not found");
        }
    }

}



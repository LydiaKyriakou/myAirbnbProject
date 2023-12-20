package com.example.newairbnb.repository;

import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.Reservation;
import com.example.newairbnb.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}

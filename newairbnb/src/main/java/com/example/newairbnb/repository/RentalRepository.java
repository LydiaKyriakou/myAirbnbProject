package com.example.newairbnb.repository;

import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.Results;
import com.example.newairbnb.user.RoleName;
import com.example.newairbnb.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>, RentalCustomRepository {

    void deleteRentalById(Long id);

    Optional<Rental> findRentalById(Long id);
    @Query("SELECT r.id FROM Rental r ")
    List<Long> findAllRentalIds();

    Page<Rental> findAllByIdIn(List<Long> rentalIds,Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM rentals r " +
            "INNER JOIN rental_availabilities ra ON r.id = ra.rental_id " +
            " WHERE ra.date >= :startDate and ra.date <= :endDate" +
            "  AND ra.is_available = 1 AND r.city = :city  AND r.max_visitors >= :guests " +
            "GROUP BY r.id HAVING COUNT(DISTINCT ra.date) = :days  ",
            nativeQuery = true)
    Page<Rental> findAvailableRentalsBetweenDates(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("city") String city,
            @Param("guests") Integer guests,
            @Param("days") Long days,
             Pageable pageable);



}
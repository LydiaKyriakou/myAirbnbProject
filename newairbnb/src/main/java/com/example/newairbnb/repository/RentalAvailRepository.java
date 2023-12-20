package com.example.newairbnb.repository;


import com.example.newairbnb.user.RentalAvail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface RentalAvailRepository extends JpaRepository<RentalAvail, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE RentalAvail ra SET ra.rental_id = :newRentalId WHERE ra.rental_id IS NULL")
    void updateNullRentalIds(Long newRentalId);
}
package com.example.newairbnb.repository;

import com.example.newairbnb.user.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


public interface ImageRepository  extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Image i SET i.rental_id = :newRentalId WHERE i.rental_id IS NULL")
    void updateNullRentalIds(Long newRentalId);

}

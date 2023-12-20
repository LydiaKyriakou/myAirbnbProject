package com.example.newairbnb.repository;

import com.example.newairbnb.user.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalCustomRepository {
    Page<Rental> sendResults(
            @Param("guests") Integer guests,
            @Param("checkin") String checkin,
            @Param("checkout") String checkout,
            @Param("city") String city,
            @Param("wifi") Boolean wifi,
            @Param("ac") Boolean ac,
            @Param("kitchen") Boolean kitchen,
            @Param("tv") Boolean tv,
            @Param("parking") Boolean parking,
            @Param("elevator") Boolean elevator,
            @Param("maxPrice") Integer maxPrice,
            @Param("days") Long days,
            @Param("type") String type,
            Pageable pageable
    );
}

package com.example.newairbnb.repository;


import com.example.newairbnb.user.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RentalCustomRepositoryImpl implements  RentalCustomRepository {
    private final EntityManager entityManager;

    @Autowired
    public RentalCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Rental> sendResults (
            Integer guests,
            String checkin,
            String checkout,
            String city,
            Boolean wifi,
            Boolean ac,
            Boolean kitchen,
            Boolean tv,
            Boolean parking,
            Boolean elevator,
            Integer maxPrice,
            Long days,
            String type,
            Pageable pageable
    ) {
        // Your SQL query goes here
        String sql = "SELECT * FROM rentals r " +
                "INNER JOIN rental_availabilities ra ON r.id = ra.rental_id " +
                "WHERE ra.date >= :checkin and ra.date <= :checkout " +
                "AND ra.is_available = true AND r.city = :city " +
                "AND r.max_visitors >= :guests " ;

        if(wifi.equals(true)){
            sql = sql +"AND r.wifi = :wifi ";
        }
        if (ac.equals(true)) {
            sql = sql +"AND r.ac = :ac " ;
        }
        if (kitchen.equals(true)) {
            sql = sql +"AND r.kitchen = :kitchen " ;
        }
        if (tv.equals(true)) {
            sql = sql +"AND r.tv = :tv " ;
        }
        if (parking.equals(true)) {
            sql = sql +"AND r.parking = :parking " ;
        }
        if (elevator.equals(true)) {
            sql = sql +"AND r.elevator = :elevator " ;
        }
        if(type != null){
            sql = sql +"AND r.type = :type " ;
        }
        if(maxPrice != 0){
            sql = sql +"AND r.min_price + ( :guests - 1)*r.plus_price_per_person <= :maxPrice " ;
        }

        sql = sql +  " GROUP BY r.id HAVING COUNT(DISTINCT ra.date) = :days ORDER BY r.min_price ASC";


        Query query = entityManager.createNativeQuery(sql, Rental.class);

        if(wifi.equals(true)){
            query.setParameter("wifi", wifi);
        }
        if (ac.equals(true)) {
            query.setParameter("ac", ac);
        }
        if (kitchen.equals(true)) {
            query.setParameter("kitchen", kitchen);
        }
        if (tv.equals(true)) {
            query.setParameter("tv", tv);
        }
        if (parking.equals(true)) {
            query.setParameter("parking", parking);
        }
        if (elevator.equals(true)) {
            query.setParameter("elevator", elevator);
        }
        if (type != null) {
            query.setParameter("type", type);
        }
        if(maxPrice != 0){
            query.setParameter("maxPrice", maxPrice);
        }
        // Set parameters
        query.setParameter("guests", guests);
        query.setParameter("checkin", checkin);
        query.setParameter("checkout", checkout);
        query.setParameter("city", city);
        query.setParameter("days", days);

        // Set pagination
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        // Execute the query and return the result as a Page
        List<Rental> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, resultList.size());
    }
}

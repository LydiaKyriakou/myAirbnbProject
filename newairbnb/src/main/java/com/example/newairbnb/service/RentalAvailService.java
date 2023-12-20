package com.example.newairbnb.service;

import com.example.newairbnb.repository.RentalAvailRepository;
import com.example.newairbnb.user.Image;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.RentalAvail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RentalAvailService {
    @Autowired
    private RentalAvailRepository rentalAvailRepository;


    public void updateNullRentalIdsToNewRental(Long newRentalId) {
        rentalAvailRepository.updateNullRentalIds(newRentalId);
    }

    public RentalAvail addAvailDate(RentalAvail rentalAvail) {
        return rentalAvailRepository.save(rentalAvail);
    }
    public List<RentalAvail> findAllRentalAvails(){
        return rentalAvailRepository.findAll();
    }

    public void deleteRentalAvail(@PathVariable  Long id) {
        rentalAvailRepository.deleteById(id);
    }
}

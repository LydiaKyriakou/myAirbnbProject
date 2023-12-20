package com.example.newairbnb.service;

import com.example.newairbnb.repository.ImageRepository;
import com.example.newairbnb.user.Image;
import com.example.newairbnb.user.Review;
import com.example.newairbnb.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
@Service
public class ImageService {
    @Autowired
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image addImage(Image image) {
        return imageRepository.save(image);
    }

    public void updateNullRentalIdsToNewRental(Long newRentalId) {
        imageRepository.updateNullRentalIds(newRentalId);
    }




    public void deleteImage(@PathVariable Long id) {
        imageRepository.deleteById(id);
    }
}

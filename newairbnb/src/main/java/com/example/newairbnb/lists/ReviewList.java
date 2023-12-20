package com.example.newairbnb.lists;

import com.example.newairbnb.user.Review;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "reviews")
public class ReviewList {

    private List<Review> reviews;

    @XmlElement(name = "review")
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

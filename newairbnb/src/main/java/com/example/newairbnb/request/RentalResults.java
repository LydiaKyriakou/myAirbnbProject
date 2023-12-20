package com.example.newairbnb.request;

public class RentalResults {
    private Long id;
    private String imageUrl;
    private Integer costPerDay;
    private String type;
    private Integer beds;
    private Integer reviewsSum;
    private Double reviewAvg;

    public RentalResults(String imageUrl, Integer costPerDay, String type, Integer beds,
                         Integer reviewsSum, Double reviewAvg) {
        this.imageUrl = imageUrl;
        this.costPerDay = costPerDay;
        this.type = type;
        this.beds = beds;
        this.reviewsSum = reviewsSum;
        this.reviewAvg = reviewAvg;
    }

    public RentalResults() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(Integer costPerDay) {
        this.costPerDay = costPerDay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getReviewsSum() {
        return reviewsSum;
    }

    public void setReviewsSum(Integer reviewsSum) {
        this.reviewsSum = reviewsSum;
    }

    public Double getReviewAvg() {
        return reviewAvg;
    }

    public void setReviewAvg(Double reviewAvg) {
        this.reviewAvg = reviewAvg;
    }
}

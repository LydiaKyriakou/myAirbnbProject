package com.example.newairbnb.controller;

import com.example.newairbnb.service.*;
import com.example.newairbnb.user.*;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/results")
public class ResultController {

    private  final UserService userService ;
    private  final RentalService rentalService ;
    private  final ReviewService reviewService ;
    private final ReservationService reservationService;
    private final ClickService clickService;

    Results results;

    public ResultController(UserService userService, RentalService rentalService, ReviewService reviewService, ReservationService reservationService, ClickService clickService) {
        this.userService = userService;
        this.rentalService = rentalService;

        this.reviewService = reviewService;
        this.reservationService = reservationService;
        this.clickService = clickService;
    }

    @GetMapping("/find/checkin/{checkin}/checkout/{checkout}/city={city}&guests={guests}&page={page}&size={size}")
    public ResponseEntity<Page<Rental>> findAvailableRentalsBetweenDates (
            @PathVariable String checkin,
            @PathVariable String checkout,
            @PathVariable String city,
            @PathVariable Integer guests,
            @PathVariable Integer page,
            @PathVariable Integer size){

        Sort sort = Sort.by(Sort.Direction.ASC, "min_price");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Rental> rentals = rentalService.findAvailableRentalsBetweenDates(checkin, checkout,city,guests,pageable);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @PostMapping("/post-filters")
    public ResponseEntity<Results> addFilterResults(@RequestBody Results results) {

        Results resultsGet = new Results(results.getGuests(), results.getCheckin(),
        results.getCheckout(),results.getCity(),results.getWifi(),results.getAc(),results.getKitchen(),
                results.getTv(),results.getParking(),results.getElevator(),results.getMaxPrice(),results.getType());
        this.results = resultsGet;
        return new ResponseEntity<>(resultsGet, HttpStatus.CREATED);
    }

    @GetMapping("/sendResults&page={page}&size={size}")
    public ResponseEntity<Page<Rental>>  sendResults(  @PathVariable Integer page,
                                                       @PathVariable Integer size){

        Sort sort = Sort.by(Sort.Direction.ASC, "min_price");
        Pageable pageable = PageRequest.of(page, size,sort);

        Page<Rental> rentals = rentalService.sendResults(results, pageable);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/sendRecommendations&guestId={guestId}&page={page}&size={size}")
    public ResponseEntity<?>  sendRecommendations(  @PathVariable Integer page,
                                                       @PathVariable Integer size,
                                                        @PathVariable Integer guestId){

        //create R table that contains the ratings every user gave on each rental
        List<Long> guest_ids = userService.getAllUserIdsWithUserRole();
        List<Long> rentals = rentalService.findAllRentalIds();
        List<Review> reviews = reviewService.findAllReviews();
        List<Reservation> reservations = reservationService.findAllReservations();
        List<Click> clicks = clickService.findAllclicks();

        //find max size for the for loop
        Integer max = Math.max(reviews.size(),reservations.size());
        max = Math.max(max, clicks.size());

        int pos = 0;
        Matrix R = new Basic2DMatrix(guest_ids.size(), rentals.size());
        while(pos < max){

            //add all the reviews with weight 4
            if(pos < reviews.size()){
                Integer i = guest_ids.indexOf(reviews.get(pos).getGuest_id());
                Integer j = rentals.indexOf(reviews.get(pos).getRental_id());
                if(i != -1 && j != -1){
                    R.set(i, j, 4*reviews.get(pos).getRating());
                }
            }

            //adds 2 points if the user has made reservation in the past
            if(pos < reservations.size()){
                Integer i = guest_ids.indexOf(reservations.get(pos).getGuest());
                Integer j = rentals.indexOf(reservations.get(pos).getRental());
                if(i != -1 && j != -1){
                    double currentValue = R.get(i, j);
                    R.set(i, j, currentValue + 2);
                }
            }

            //adds 0.5 points if the user had click in the past
            if(pos < clicks.size()){
                Integer i = guest_ids.indexOf(clicks.get(pos).getGuest_id());
                Integer j = rentals.indexOf(clicks.get(pos).getRental_id());
                if(i != -1 && j != -1){
                    double currentValue = R.get(i, j);
                    R.set(i, j, currentValue + 0.5);
                }
            }

            pos++;
        }

        //fills the blank ratings
        MatrixFactorization matrixFactorization = new MatrixFactorization();
        Matrix nPQ = matrixFactorization.run(R);

        Integer userPos = guest_ids.indexOf(Long.valueOf(guestId));
        Map<Long, Double> rentalRatingsMap = new HashMap<>();
        for (int i = 0; i < rentals.size(); i++) {
            Long rentalId = rentals.get(i);
            if(userPos == -1){
                return new ResponseEntity<>( HttpStatus.FORBIDDEN);
            }
            Double rating =  nPQ.get(userPos ,i);
            rentalRatingsMap.put(rentalId, rating);
        }

        // Sort the map by values in descending order
        Map<Long, Double> sortedMap = rentalRatingsMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        for (Map.Entry<Long, Double> entry : sortedMap.entrySet()) {
            Long id = entry.getKey();
            Double value = entry.getValue();
            System.out.println(id + ", " + value);
        }


        List<Long> recommendedRentalIds = new ArrayList<>(sortedMap.keySet());
        List<Rental> recommendedRentals = new ArrayList<>();


        for (Long rentalId : recommendedRentalIds) {
            Optional<Rental> rentalOptional = rentalService.getRental(rentalId);
            rentalOptional.ifPresent(recommendedRentals::add);
            if(recommendedRentals.size() == 10){
                break;
            }
        }

        return new ResponseEntity<>(recommendedRentals, HttpStatus.OK);
    }






}

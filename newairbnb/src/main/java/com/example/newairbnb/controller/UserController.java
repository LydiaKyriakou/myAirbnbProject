package com.example.newairbnb.controller;

import com.example.newairbnb.lists.RentalList;
import com.example.newairbnb.lists.ReservationList;
import com.example.newairbnb.lists.ReviewList;
import com.example.newairbnb.lists.UserList;
import com.example.newairbnb.security.*;
import com.example.newairbnb.service.*;
import com.example.newairbnb.user.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RentalService rentalService;
    private final ReservationService reservationService;
    private final RentalAvailService rentalAvailService;
    private final ReviewService reviewService;

    private final ImageService imageService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    public UserController(UserService userService, RentalService rentalService, ReservationService reservationService, RentalAvailService rentalAvailService, ReviewService reviewService, ImageService imageService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.rentalService = rentalService;
        this.reservationService = reservationService;
        this.rentalAvailService = rentalAvailService;
        this.reviewService = reviewService;
        this.imageService = imageService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok().body("{}");
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all&page={page}&size={size}")
    public ResponseEntity<Page<User>> getUsers (@PathVariable Integer page,
                                                @PathVariable Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/hosts")
    public ResponseEntity<List<Long>> getAllUserIdsWithUserRole () {
        List<Long> hosts = userService.getAllUserIdsWithUserRole();
        return new ResponseEntity<>(hosts, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById (@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HOST')")
    @PostMapping("/{username}/add-rental")
    public ResponseEntity<Rental> addRentalByHost(@PathVariable("username") String username, @RequestBody Rental rental) {
        Optional<User> optionalHost = userService.findByUsername(username);
        if (optionalHost.isPresent()) {
            User host = optionalHost.get();
            rental.setHost(host.getId());

            Rental newRental = rentalService.addRental(rental);
            rentalAvailService.updateNullRentalIdsToNewRental(newRental.getId());
            imageService.updateNullRentalIdsToNewRental(newRental.getId());

            return new ResponseEntity<>(newRental, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userService.getUser(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            boolean changePassword= true;
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }else{
                changePassword = false;
            }
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }

            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }

            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getPhone() != null) {
                existingUser.setPhone(updatedUser.getPhone());
            }

            if (updatedUser.getImageUrl() != null) {
                existingUser.setImageUrl(updatedUser.getImageUrl());
            }

            User savedUser = userService.updateUser(id,existingUser,changePassword);

            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{id}")
    public ResponseEntity<User> approveHost(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.getUser(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            existingUser.setApproval(true); // Set approval status to true
            User savedUser = userService.updateUser(id,existingUser,false);

            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllUsers () {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}







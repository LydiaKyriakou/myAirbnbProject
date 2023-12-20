package com.example.newairbnb.controller;

import com.example.newairbnb.service.ClickService;
import com.example.newairbnb.user.Click;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/click")
public class clickController {

    private final ClickService clickService;

    public clickController(ClickService clickService) {
        this.clickService = clickService;
    }

    @PostMapping("/add")
    public ResponseEntity<Click> addClick(@RequestBody Click click) {
        Click newClick = clickService.addClick(click);
        return new ResponseEntity<>(newClick, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Click>> findAllClicks () {
        List<Click> clicks = clickService.findAllclicks();
        return new ResponseEntity<>(clicks, HttpStatus.OK);
    }

}

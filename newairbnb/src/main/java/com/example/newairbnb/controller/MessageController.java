package com.example.newairbnb.controller;

import com.example.newairbnb.request.MessageUsers;
import com.example.newairbnb.service.MessageService;
import com.example.newairbnb.service.UserService;
import com.example.newairbnb.user.Message;
import com.example.newairbnb.user.Rental;
import com.example.newairbnb.user.Review;
import com.example.newairbnb.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user/messages")
public class MessageController {
    private final MessageService messageService;
    private  final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    @GetMapping("/show/sender={user1}&receiver={user2}")
    public ResponseEntity<List<Message>> getConversationByUsersId(@PathVariable Long user1, @PathVariable Long user2) {
        List<Message> messages = messageService.getConversationByUsersId(user1,user2);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    @PostMapping("/add/sender={senderId}&receiver={receiverId}")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        User sender = userService.findUserById(message.getSender());
        User receiver = userService.findUserById(message.getReceiver());
        Long senderId = sender.getId();
        Long receiverId = receiver.getId();
        sender.addMessage(message);
        receiver.addMessage(message);
        Message newMessage = messageService.addMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    @GetMapping("/show/receiversfrom={userId}")
    public ResponseEntity<Set<Long>>  getAllReceiversByUserId(@PathVariable  Long userId){
        Set<Long> receiver_ids = userService.getAllReceiversByUserId(userId);
        return new ResponseEntity<>(receiver_ids, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") Long id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}

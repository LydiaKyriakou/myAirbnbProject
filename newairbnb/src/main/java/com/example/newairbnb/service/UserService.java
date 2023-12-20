package com.example.newairbnb.service;

import com.example.newairbnb.exception.UserNotFoundException;
import com.example.newairbnb.repository.MessageRepository;
import com.example.newairbnb.repository.RoleRepository;
import com.example.newairbnb.repository.UserRepository;
import com.example.newairbnb.user.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final MessageRepository messageRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, MessageRepository messageRepositoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.messageRepository = messageRepositoryRepository;
    }


public User addUser(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
        throw new UserNotFoundException("Username is already taken");
    }

    if (userRepository.existsByEmail(user.getEmail())) {
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
        throw new UserNotFoundException("Email is already taken");
    }

    Role role = user.getRoles().get(0);
    String type = role.getName().toString();

    if(type.equals("ROLE_USER")){
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new UserNotFoundException("User role not set"));
        user.setRoles(Collections.singletonList(userRole));

    }else{
        Role userRole = roleRepository.findByName(RoleName.ROLE_HOST)
                .orElseThrow(() -> new UserNotFoundException("User role not set"));
        user.setRoles(Collections.singletonList(userRole));
        user.setApproval(false);
    }

    String hashedPassword = passwordEncoder.encode(user.getPassword()); // Hash the password
    user.setPassword(hashedPassword);

    return userRepository.save(user);
}


    public Page<User> getUsers(Pageable pageable){
        return userRepository.getUsers(pageable);
    }


    public List<Long> getAllUserIdsWithUserRole() {
        // Query the database to fetch user IDs of users with ROLE_USER
        List<Long> userIds = userRepository.findUserIdsByRole(RoleName.ROLE_USER);

        return userIds;
    }

    public List<Long> getAllUserIdsWithHostRole() {
        // Query the database to fetch user IDs of users with ROLE_USER
        List<Long> userIds = userRepository.findUserIdsByRole(RoleName.ROLE_HOST);

        return userIds;
    }

    public Optional<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id);
    }



    public User updateUser(Long userId, User updatedUser, Boolean changePassword) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        // Update the fields with the new values
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (changePassword == true) {
            String hashedPassword = passwordEncoder.encode(updatedUser.getPassword()); // Hash the password
            existingUser.setPassword(hashedPassword);
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


        // Save the updated user
        return userRepository.save(existingUser);
    }

    public Set<Long> getAllReceiversByUserId(Long userId) {
        List<Message> messages = messageRepository.findAll();

        // Extract the receiver IDs from the messages
        Set<Long> uniqueSenderIdsToReceiver = messages.stream()
                .filter(message -> message.getReceiver().equals(userId))
                .map(Message::getSender)
                .collect(Collectors.toSet());

        return uniqueSenderIdsToReceiver;
    }


    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }



    public Optional<User> findHostByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}








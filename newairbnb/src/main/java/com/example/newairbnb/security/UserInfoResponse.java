package com.example.newairbnb.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}

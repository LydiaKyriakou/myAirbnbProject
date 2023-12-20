package com.example.newairbnb.repository;


import com.example.newairbnb.user.Role;
import com.example.newairbnb.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
package com.example.newairbnb.repository;

import com.example.newairbnb.user.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ClickRepository extends JpaRepository<Click,Long> {
}

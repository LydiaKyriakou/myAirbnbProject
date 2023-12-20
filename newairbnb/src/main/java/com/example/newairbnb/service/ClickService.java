package com.example.newairbnb.service;

import com.example.newairbnb.repository.ClickRepository;
import com.example.newairbnb.user.Click;
import com.example.newairbnb.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClickService {
    private final ClickRepository clickRepository;

    @Autowired
    public ClickService(ClickRepository clickRepository) {
        this.clickRepository = clickRepository;
    }

    public Click addClick(Click click){
        return clickRepository.save(click);
    }

    public List<Click> findAllclicks(){
        return  clickRepository.findAll();
    }
}

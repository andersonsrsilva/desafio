package com.desafio.service;

import com.desafio.exception.ResourceNotFoundException;
import com.desafio.model.User;
import com.desafio.repository.TimeSheetRepository;
import com.desafio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimeSheetService {

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private UserRepository userRepository;

    public void mark(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(!userOptional.isPresent()) {
           throw new ResourceNotFoundException("User not found");
        }


    }

}

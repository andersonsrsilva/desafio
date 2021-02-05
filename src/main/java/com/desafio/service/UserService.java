package com.desafio.service;

import com.desafio.domain.User;
import com.desafio.repository.UserRepository;
import com.desafio.service.dto.UserDTO;
import com.desafio.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        List<UserDTO> listDTO = MapperUtils.mapAll(list, UserDTO.class);

        return listDTO;

    }
}

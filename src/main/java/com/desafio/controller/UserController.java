package com.desafio.controller;

import com.desafio.service.UserService;
import com.desafio.service.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "User Controller")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @ApiOperation(value = "List my users")
    public ResponseEntity<?> findAll() {
        List<UserDTO> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}

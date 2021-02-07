package com.desafio.controller;

import com.desafio.service.UserService;
import com.desafio.service.dto.ProjectHourDTO;
import com.desafio.service.dto.UserDTO;
import com.desafio.service.dto.UserInDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "User Controller")
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

    @PostMapping()
    @ApiOperation(value = "Register a new user")
    public ResponseEntity<?> create(@RequestBody UserInDTO dto) {
        userService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}

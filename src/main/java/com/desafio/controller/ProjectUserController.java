package com.desafio.controller;

import com.desafio.service.ProjectUserService;
import com.desafio.service.dto.ProjectUserNewInDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Project-User Controller")
@RequestMapping("/api/project-users")
public class ProjectUserController {

    @Autowired
    private ProjectUserService projectUserService;

    @PostMapping()
    @ApiOperation(value = "Binding project with user")
    public ResponseEntity<?> create(@RequestBody ProjectUserNewInDTO dto) {
        projectUserService.binding(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

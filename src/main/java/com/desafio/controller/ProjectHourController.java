package com.desafio.controller;

import com.desafio.service.ProjectHourService;
import com.desafio.service.dto.ProjectHourDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-hour")
public class ProjectHourController {

    @Autowired
    private ProjectHourService projectHourService;

    @PostMapping()
    @ResponseStatus( HttpStatus.CREATED )
    public ResponseEntity<?> register(@RequestBody ProjectHourDTO dto) {
        projectHourService.register(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

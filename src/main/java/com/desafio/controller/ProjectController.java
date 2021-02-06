package com.desafio.controller;

import com.desafio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{idUser}/my-projects")
    @ResponseStatus( HttpStatus.CREATED )
    public ResponseEntity<?> myProjects(@PathVariable Long idUser) {
        projectService.myProjects(idUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

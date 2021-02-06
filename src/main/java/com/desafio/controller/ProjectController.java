package com.desafio.controller;

import com.desafio.service.ProjectService;
import com.desafio.service.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{idUser}/my-projects")
    @ResponseStatus( HttpStatus.CREATED )
    public ResponseEntity<?> myProjects(@PathVariable Long idUser) {
        List<ProjectDTO> list = projectService.myProjects(idUser);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}

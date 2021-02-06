package com.desafio.controller;

import com.desafio.service.ProjectService;
import com.desafio.service.dto.ProjectDTO;
import com.desafio.service.dto.ProjectHourDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Project Controller")
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{idUser}/my-projects")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "List my project")
    public ResponseEntity<?> myProjects(@PathVariable Long idUser) {
        List<ProjectDTO> list = projectService.myProjects(idUser);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/register-hours")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register hour in a project")
    public ResponseEntity<?> registerHours(@Valid @RequestBody ProjectHourDTO dto) {
        projectService.registerHours(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

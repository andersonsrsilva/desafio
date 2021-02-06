package com.desafio.controller;

import com.desafio.service.TimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timesheets")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetsService;

    @GetMapping("/{idUser}/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@PathVariable Long idUser) {
        timeSheetsService.register(idUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

package com.desafio.controller;

import com.desafio.service.TimeSheetService;
import com.desafio.service.dto.TimeSheetDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Api(value = "Timesheet Controller")
@RequestMapping("/api/timesheets")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetsService;

    @GetMapping("/{idUser}/register")
    @ApiOperation(value = "Register my timesheet")
    public ResponseEntity<?> register(@PathVariable Long idUser) {
        timeSheetsService.register(idUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{idUser}/{date}")
    @ApiOperation(value = "Get my timesheet by date")
    public ResponseEntity<?> getByTime(@ApiParam(value = "ID User", example = "") @PathVariable Long idUser,
                                       @ApiParam(value = "Date (yyyy-MM-dd)", example = "")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        @PathVariable LocalDate date) {
        List<TimeSheetDTO> list = timeSheetsService.getByTime(idUser, date);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edit a timesheet")
    public ResponseEntity<?> edit(@Valid @RequestBody TimeSheetDTO dto, @PathVariable Long id) {
        timeSheetsService.edit(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

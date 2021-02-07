package com.desafio.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetInDTO {

    private Long id;

    @ApiModelProperty(example = "HH:mm")
    private LocalTime record;

}

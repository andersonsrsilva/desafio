package com.desafio.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetDTO {

    private Long id;
    private Long user;

    @ApiModelProperty(example = "yyyy-MM-dd HH:mm")
    private LocalDateTime record;

}

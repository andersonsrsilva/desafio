package com.desafio.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectHourDTO {

    private Long projectId;

    private Long userId;

    @ApiModelProperty(example = "yyyy-MM-dd")
    private LocalDate recordDate;

    @Positive(message = "hour invalid")
    private Integer hour;

}

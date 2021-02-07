package com.desafio.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserNewInDTO {

    private Long idProject;
    private Long idUser;

}

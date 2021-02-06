package com.desafio.service;

import com.desafio.repository.ProjectHourRepository;
import com.desafio.service.dto.ProjectHourDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectHourService {

    @Autowired
    private ProjectHourRepository projectHourRepository;

    public void register(ProjectHourDTO dto) {
        String name = "";
    }

}

package com.desafio.service;

import com.desafio.domain.Project;
import com.desafio.domain.ProjectUser;
import com.desafio.domain.User;
import com.desafio.repository.ProjectRepository;
import com.desafio.repository.ProjectUserRepository;
import com.desafio.repository.UserRepository;
import com.desafio.service.dto.ProjectDTO;
import com.desafio.service.dto.UserDTO;
import com.desafio.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public List<ProjectDTO> myProjects(Long idUser) {
        List<ProjectUser> projects = projectUserRepository.findProjectsByIdUser(idUser);

        List<ProjectDTO> list = projects.stream().map(
                p -> new ProjectDTO(p.getId(), p.getProject().getName())
        ).collect(Collectors.toList());

        return list;
    }
}

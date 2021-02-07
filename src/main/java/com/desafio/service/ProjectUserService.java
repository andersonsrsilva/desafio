package com.desafio.service;

import com.desafio.domain.Project;
import com.desafio.domain.ProjectUser;
import com.desafio.domain.User;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.repository.ProjectRepository;
import com.desafio.repository.ProjectUserRepository;
import com.desafio.repository.UserRepository;
import com.desafio.service.dto.ProjectUserNewInDTO;
import com.desafio.service.dto.UserDTO;
import com.desafio.service.dto.UserInDTO;
import com.desafio.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        List<UserDTO> listDTO = MapperUtils.mapAll(list, UserDTO.class);

        return listDTO;
    }

    public void create(UserInDTO dto) {
        User user = MapperUtils.map(dto, User.class);
        userRepository.save(user);
    }

    public void binding(ProjectUserNewInDTO dto) {
        Optional<User> userOptional = userRepository.findById(dto.getIdUser());

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        Optional<Project> optionalProject = projectRepository.findById(dto.getIdProject());

        if (!optionalProject.isPresent()) {
            throw new ResourceNotFoundException("Project not found.");
        }

        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(optionalProject.get());
        projectUser.setUser(userOptional.get());
        projectUserRepository.save(projectUser);
    }
}

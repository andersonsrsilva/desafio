package com.desafio.service;

import com.desafio.domain.ProjectHour;
import com.desafio.domain.ProjectUser;
import com.desafio.domain.TimeSheet;
import com.desafio.domain.User;
import com.desafio.enums.Number;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.exception.ValidationException;
import com.desafio.repository.*;
import com.desafio.service.dto.ProjectDTO;
import com.desafio.service.dto.ProjectHourDTO;
import com.desafio.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectHourRepository projectHourRepository;

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public List<ProjectDTO> myProjects(Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        List<ProjectUser> projects = projectUserRepository.findProjectsByIdUser(idUser);

        List<ProjectDTO> projectDTOList = projects.stream().map(
                p -> new ProjectDTO(p.getId(), p.getProject().getName())
        ).collect(Collectors.toList());

        return projectDTOList;
    }

    public void registerHours(ProjectHourDTO dto) {
        validation(dto);

        ProjectUser projectUser = projectUserRepository.findByProjectUser(dto.getProjectId(), dto.getUserId());
        ProjectHour projectHour = new ProjectHour();
        projectHour.setHours(dto.getHour());
        projectHour.setDate(dto.getDate());
        projectHour.setProjectUser(projectUser);
        projectHourRepository.save(projectHour);
    }

    private void validation(ProjectHourDTO dto) {
        Optional<User> userOptional = userRepository.findById(dto.getUserId());

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        LocalDateTime initialDate = DateUtil.initialDate(dto.getDate());
        LocalDateTime finalDate = DateUtil.finalDate(dto.getDate());

        List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDay(initialDate, finalDate, dto.getUserId());

        if(timeSheetList.isEmpty()) {
            throw new ValidationException("You do not have worked this day");
        }

        if(timeSheetList.size() == Number.ONE.getValue() || timeSheetList.size() == Number.TREE.getValue()) {
            throw new ValidationException("Please, adjust your time sheet");
        }

        int totalHour = 0;

        TimeSheet timeSheet1 = timeSheetList.get(0);
        TimeSheet timeSheet2 = timeSheetList.get(1);

        totalHour += DateUtil.diffMinutes(timeSheet1.getRecord(), timeSheet2.getRecord());

        if(timeSheetList.size() == Number.FOUR.getValue()) {
            TimeSheet timeSheet3 = timeSheetList.get(2);
            TimeSheet timeSheet4 = timeSheetList.get(3);

            totalHour += DateUtil.diffMinutes(timeSheet3.getRecord(), timeSheet4.getRecord());
        }

        ProjectUser projectUser = projectUserRepository.findByProjectUser(dto.getProjectId(), dto.getUserId());
        int registeHours = projectHourRepository.findRegisteHours(projectUser, dto.getDate());

        int total = (dto.getHour() + registeHours) * Number.SIXTY.getValue();

        if(total > totalHour) {
            throw new ValidationException("Insufficient hours worked");
        }
    }

}

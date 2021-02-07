package com.desafio.service;

import com.desafio.domain.ProjectUser;
import com.desafio.domain.TimeSheet;
import com.desafio.domain.User;
import com.desafio.enums.Number;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.exception.ValidationException;
import com.desafio.repository.ProjectHourRepository;
import com.desafio.repository.ProjectUserRepository;
import com.desafio.repository.TimeSheetRepository;
import com.desafio.repository.UserRepository;
import com.desafio.service.dto.TimeSheetInDTO;
import com.desafio.service.dto.TimeSheetOutDTO;
import com.desafio.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSheetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private ProjectHourRepository projectHourRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public void register(Long idUser) {
        if (isWeekend()) {
            throw new ValidationException("Not allowed on weekends.");
        }

        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDayByUser(DateUtil.initialDate(), DateUtil.finalDate(), userOptional.get());

        if (timeSheetList.isEmpty() || timeSheetList.size() == Number.ONE.getValue() || timeSheetList.size() == Number.TREE.getValue()) {
            addRecord(userOptional.get());
        } else if (timeSheetList.size() == Number.TWO.getValue()) {
            if(isMinOneHour(timeSheetList)) {
                throw new ValidationException("Not allowed register. One hour to the lunch.");
            }

            addRecord(userOptional.get());
        }else {
            throw new ValidationException("Not allowed register. Timesheet completed");
        }
    }

    private void addRecord(User user) {
        TimeSheet timeSheet = TimeSheet.builder()
                .user(user)
                .record(LocalDateTime.now())
                .build();

        timeSheetRepository.save(timeSheet);
    }

    private boolean isMinOneHour(List<TimeSheet> list) {
        Optional<TimeSheet> lastElement = list.stream().reduce((first, second) -> second);
        return DateUtil.diffMinutes(lastElement.get().getRecord()) < Number.SIXTY.getValue();
    }

    private boolean isWeekend() {
        switch (LocalDate.now().getDayOfWeek()) {
            case SATURDAY:
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }

    public void edit(@Valid Long id, Long idUser, LocalTime hour) {
        validationEdit(id, idUser, hour);


    }

    private void validationEdit(Long id, Long idUser, LocalTime hour) {
        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        Optional<TimeSheet> timeSheetOptional = timeSheetRepository.findById(id);

        if(!timeSheetOptional.get().getUser().equals(userOptional.get())){
            throw new ResourceNotFoundException("User not found for this timesheet.");
        }

        int total = projectHourRepository.findRegisterHoursByDate(userOptional.get(), timeSheetOptional.get().getRecord().toLocalDate());

        if(total > 0) {
            LocalDateTime initialDate = DateUtil.initialDate(timeSheetOptional.get().getRecord().toLocalDate());
            LocalDateTime finalDate = DateUtil.finalDate(timeSheetOptional.get().getRecord().toLocalDate());

            List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDayByUser(initialDate, finalDate, userOptional.get());

            int totalHour = 0;

            TimeSheet timeSheet1 = timeSheetList.get(0);
            TimeSheet timeSheet2 = timeSheetList.get(1);

            totalHour += DateUtil.diffMinutes(timeSheet1.getRecord(), timeSheet2.getRecord());

            if (timeSheetList.size() == Number.FOUR.getValue()) {
                TimeSheet timeSheet3 = timeSheetList.get(2);
                TimeSheet timeSheet4 = timeSheetList.get(3);

                totalHour += DateUtil.diffMinutes(timeSheet3.getRecord(), timeSheet4.getRecord());
            }

            //REVISAR
            if ((total * Number.SIXTY.getValue()) > totalHour) {
                throw new ValidationException("Longer time worked");
            }
        }

    }

    public List<TimeSheetOutDTO> getByTime(Long idUser, LocalDate date) {
        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        LocalDateTime initialDate = DateUtil.initialDate(date);
        LocalDateTime finalDate = DateUtil.finalDate(date);

        List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDayByUser(initialDate, finalDate, userOptional.get());

        List<TimeSheetOutDTO> list = timeSheetList.stream().map(
                p -> new TimeSheetOutDTO(p.getId(), DateUtil.dateToString(p.getRecord()))).collect(Collectors.toList());

        return list;
    }

}





















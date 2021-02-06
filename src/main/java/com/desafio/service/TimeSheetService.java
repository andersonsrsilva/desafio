package com.desafio.service;

import com.desafio.domain.TimeSheet;
import com.desafio.domain.User;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.exception.ValidationException;
import com.desafio.repository.TimeSheetRepository;
import com.desafio.repository.UserRepository;
import com.desafio.util.DateUtil;
import com.desafio.enums.Number;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSheetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    public void register(Long idUser) {
        if (isWeekend()) {
            throw new ValidationException("Not allowed on weekends.");
        }

        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDay(DateUtil.initialDate(), DateUtil.finalDate(), idUser);

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

}





















package com.desafio.service;

import com.desafio.domain.TimeSheet;
import com.desafio.domain.User;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.exception.ValidationException;
import com.desafio.repository.TimeSheetRepository;
import com.desafio.repository.UserRepository;
import com.desafio.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSheetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    public void record(Long id) {
        if (isWeekend()) {
            throw new ValidationException("Not allowed on weekends");
        }

        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }

        List<TimeSheet> list = timeSheetRepository.findRecordPerDay(DateUtil.initialDate(), DateUtil.finalDate());

        if (list.isEmpty()) {
            addRecord(userOptional.get());
        }

    }

    private void addRecord(User user) {
        TimeSheet timeSheet = TimeSheet.builder()
                .user(user)
                .record(LocalDateTime.now())
                .build();

        timeSheetRepository.save(timeSheet);
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





















package com.desafio.service;

import com.desafio.domain.TimeSheet;
import com.desafio.domain.User;
import com.desafio.enums.Number;
import com.desafio.exception.ResourceNotFoundException;
import com.desafio.exception.ValidationException;
import com.desafio.repository.ProjectHourRepository;
import com.desafio.repository.ProjectUserRepository;
import com.desafio.repository.TimeSheetRepository;
import com.desafio.repository.UserRepository;
import com.desafio.service.dto.ReportDTO;
import com.desafio.service.dto.TimeSheeRegisterDTO;
import com.desafio.service.dto.TimeSheetOutDTO;
import com.desafio.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.time.temporal.TemporalAdjusters.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    public void register(TimeSheeRegisterDTO dto) {
        if (isWeekend()) {
            throw new ValidationException("Not allowed on weekends.");
        }

        Optional<User> userOptional = userRepository.findById(dto.getId());

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDayByUser(DateUtil.initialDate(), DateUtil.finalDate(), userOptional.get());

        if (timeSheetList.isEmpty() || timeSheetList.size() == Number.ONE.getValue() || timeSheetList.size() == Number.TREE.getValue()) {
            addRecord(userOptional.get());
        } else if (timeSheetList.size() == Number.TWO.getValue()) {
            if (isMinOneHour(timeSheetList)) {
                throw new ValidationException("Not allowed register. One hour to the lunch.");
            }

            addRecord(userOptional.get());
        } else {
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

        Optional<TimeSheet> timeSheetOptional = timeSheetRepository.findById(id);
        TimeSheet timeSheet = timeSheetOptional.get();

        LocalDateTime newDate = LocalDateTime.of(timeSheet.getRecord().toLocalDate(), hour);
        timeSheet.setRecord(newDate);

        timeSheetRepository.save(timeSheet);
    }

    private void validationEdit(Long id, Long idUser, LocalTime hour) {
        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        Optional<TimeSheet> timeSheetOptional = timeSheetRepository.findById(id);

        User user = userOptional.get();
        TimeSheet timeSheet = timeSheetOptional.get();

        if (!timeSheet.getUser().equals(user)) {
            throw new ResourceNotFoundException("User not found for this timesheet.");
        }

        if (hour.isBefore(timeSheet.getRecord().toLocalTime())) {
            return;
        }

        int launchedHour = projectHourRepository.findRegisterHoursByDate(user, timeSheet.getRecord().toLocalDate());

        LocalDateTime initialDate = DateUtil.initialDate(timeSheet.getRecord().toLocalDate());
        LocalDateTime finalDate = DateUtil.finalDate(timeSheet.getRecord().toLocalDate());
        List<TimeSheet> timeSheetList = timeSheetRepository.findRecordDayByUser(initialDate, finalDate, user);

        validLauncherHours(id, hour, launchedHour, timeSheetList);
        validIn(id, hour, timeSheetList);
        validLunchTime(timeSheetList);
    }

    private void validLunchTime(List<TimeSheet> timeSheetList) {
        TimeSheet outMorning = timeSheetList.get(1);
        TimeSheet inAfternoon = timeSheetList.get(2);

        int lunchTime = DateUtil.diffMinutes(outMorning.getRecord(), inAfternoon.getRecord());

        if(lunchTime < 60) {
            throw new ResourceNotFoundException("Min one hour to the lunch.");
        }
    }

    private void validIn(Long id, LocalTime hour, List<TimeSheet> timeSheetList) {
        TimeSheet editabled = null;
        boolean flag = false;

        for (TimeSheet t : timeSheetList) {
            if (t.getId().equals(id)) {
                t.setRecord(DateUtil.changeTime(t.getRecord(), hour));
                editabled = t;
                flag = true;
            }
            if (flag) {
                if(editabled.getRecord().isAfter(t.getRecord())) {
                    throw new ResourceNotFoundException("In after out.");
                }
                break;
            }
        }
    }

    private void validLauncherHours(Long id, LocalTime hour, int launchedHour, List<TimeSheet> timeSheetList) {
        if (timeSheetList.isEmpty()) {
            throw new ValidationException("You do not have worked this day");
        }

        if (timeSheetList.size() == Number.ONE.getValue() || timeSheetList.size() == Number.TREE.getValue()) {
            throw new ValidationException("Please, adjust your time sheet");
        }

        timeSheetList.forEach(t -> {
            if (t.getId().equals(id)) {
                t.setRecord(DateUtil.changeTime(t.getRecord(), hour));
            }
        });

        if (launchedHour > 0) {
            int totalRecordedHour = 0;

            TimeSheet inMorning = timeSheetList.get(0);
            TimeSheet outMorning = timeSheetList.get(1);

            totalRecordedHour += DateUtil.diffMinutes(inMorning.getRecord(), outMorning.getRecord());

            if (timeSheetList.size() == Number.FOUR.getValue()) {
                TimeSheet inAfternoon = timeSheetList.get(2);
                TimeSheet outAfternoon = timeSheetList.get(3);

                totalRecordedHour += DateUtil.diffMinutes(inAfternoon.getRecord(), outAfternoon.getRecord());
            }

            int launchedHourMinutes = launchedHour * Number.SIXTY.getValue();

            if ((totalRecordedHour - launchedHourMinutes) < 0) {
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

    public String reportByIdMouth(Long idUser, Integer year, Integer mouth) {
        Optional<User> userOptional = userRepository.findById(idUser);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        LocalDateTime from = LocalDateTime.of(LocalDate.of(year, mouth, 1), LocalTime.of(0,0));
        LocalDateTime to = LocalDateTime.of(from.toLocalDate().with(lastDayOfMonth()), LocalTime.of(23,59));

        List<TimeSheet> timeSheetList = timeSheetRepository.reportByIdMouth(from, to, userOptional.get());

        List<ReportDTO> reportDTOList = new ArrayList<>();
        LocalDate lastDate = null;
        ReportDTO report = new ReportDTO();

        for(TimeSheet timeSheet: timeSheetList) {
            if (lastDate == null) {
                report.setDate(timeSheet.getRecord().toLocalDate());
                report.getDateTimes().add(timeSheet.getRecord());
                reportDTOList.add(report);
                lastDate = timeSheet.getRecord().toLocalDate();
            } else {
                if (lastDate.equals(timeSheet.getRecord().toLocalDate())) {
                    report.getDateTimes().add(timeSheet.getRecord());
                } else {
                    report = new ReportDTO();
                    report.setDate(timeSheet.getRecord().toLocalDate());
                    report.getDateTimes().add(timeSheet.getRecord());
                    reportDTOList.add(report);
                    lastDate = timeSheet.getRecord().toLocalDate();
                }
            }
        }

        int totalRecordedHour = 0;

        for(ReportDTO dto: reportDTOList) {
            try {
                LocalDateTime inMorning = dto.getDateTimes().get(0);
                LocalDateTime outMorning = dto.getDateTimes().get(1);
                totalRecordedHour += DateUtil.diffMinutes(inMorning, outMorning);

                LocalDateTime inAfternoon = dto.getDateTimes().get(2);
                LocalDateTime outAfternoon = dto.getDateTimes().get(3);
                totalRecordedHour += DateUtil.diffMinutes(inAfternoon, outAfternoon);
            } catch (IndexOutOfBoundsException e) {
                throw new ResourceNotFoundException("Timesheet incompleted.");
            }
        }

        int total = Number.TOTAL_MONTH.getValue() * 60;
        int diff = totalRecordedHour - total;

        if(totalRecordedHour >= total) {
            return "Worked more: " + getTimeConverted(diff);
        }else {
            return "Worked less: " + getTimeConverted(diff * -1);
        }
    }

    private String getTimeConverted(int value) {
        int minutes = value % 60;
        int hours = (value - minutes) / 60;

        String strHours = hours == 0 ? "hour" : "hours";
        String strMinutes = minutes == 0 ? "minute" : "minutes";

        StringBuilder srt = new StringBuilder();
        srt.append(hours);
        srt.append(strHours);
        srt.append(" ");
        srt.append(minutes);
        srt.append(strMinutes);
        return srt.toString();
    }

}





















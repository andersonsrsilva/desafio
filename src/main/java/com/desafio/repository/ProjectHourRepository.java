package com.desafio.repository;

import com.desafio.domain.ProjectHour;
import com.desafio.domain.ProjectUser;
import com.desafio.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProjectHourRepository extends JpaRepository<ProjectHour, Long> {

    @Query("SELECT sum(ph.hours) FROM ProjectHour ph WHERE ph.projectUser = ?1 AND ph.recordDate = ?2")
    Integer findRegisterHours(ProjectUser idprojectUser, LocalDate date);

    @Query("SELECT sum(ph.hours) FROM ProjectHour ph INNER JOIN ph.projectUser pu WHERE pu.user = ?1 AND ph.recordDate = ?2")
    Integer findRegisterHoursByDate(User user, LocalDate date);

}
package com.desafio.repository;

import com.desafio.domain.ProjectHour;
import com.desafio.domain.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProjectHourRepository extends JpaRepository<ProjectHour, Long> {

    @Query("SELECT sum(ph.hours) FROM ProjectHour ph WHERE ph.projectUser = ?1 AND ph.date = ?2")
    int findRegisterHours(ProjectUser idprojectUser, LocalDate date);

}
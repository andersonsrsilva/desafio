package com.desafio.repository;

import com.desafio.domain.ProjectHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHourRepository extends JpaRepository<ProjectHour, Long> {

}
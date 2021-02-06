package com.desafio.repository;

import com.desafio.domain.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {

    List<TimeSheet> findByUserId(Long id);

    @Query("SELECT t FROM TimeSheet t INNER JOIN t.user u WHERE t.record > ?1 and t.record < ?2 AND u.id = ?3 ORDER BY t.record")
    List<TimeSheet> findRecordDay(LocalDateTime from, LocalDateTime to, Long idUser);

}
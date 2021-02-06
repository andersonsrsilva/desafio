package com.desafio.repository;

import com.desafio.domain.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    List<ProjectUser> findByUserId(Long idUser);
}
package com.desafio.repository;

import com.desafio.domain.Project;
import com.desafio.domain.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    List<ProjectUser> findByUserId(Long idUser);


    @Query("SELECT pu FROM ProjectUser pu INNER JOIN pu.user u WHERE u.id = ?1")
    List<ProjectUser> findProjectsByIdUser(Long idUser);

}
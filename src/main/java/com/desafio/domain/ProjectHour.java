package com.desafio.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "project_hour")
public class ProjectHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_user_id")
    private ProjectUser projectUser;

    @Column(name = "hours", nullable = false)
    private Integer hours;

}

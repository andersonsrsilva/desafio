package com.desafio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "timeSheet")
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
package com.shedule.shedule_bot.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class TeacherRang {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String rangName;


    public TeacherRang(String rangName) {
        this.rangName = rangName;
    }

    public TeacherRang() {
    }
}

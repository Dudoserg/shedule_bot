package com.shedule.shedule_bot.entity.Db.Shedule;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String subjectName;

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }
    public Subject() {
    }


}

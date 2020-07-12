package com.shedule.shedule_bot.entity.Db;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class SubjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String name;

    public SubjectType() {
    }
    public SubjectType(String name) {
        this.name = name;
    }
}

package com.shedule.shedule_bot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity

public class TimeSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private Integer timeStart;
    private Integer timeEnd;

    private String timeStartStr;
    private String timeEndStr;

    public TimeSubject() {
    }

    public TimeSubject(Integer timeStart, Integer timeEnd, String timeStartStr, String timeEndStr) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.timeStartStr = timeStartStr;
        this.timeEndStr = timeEndStr;
    }


    public String getStartToEndStr() {
        return timeStartStr + " : " + timeEndStr;
    }
}

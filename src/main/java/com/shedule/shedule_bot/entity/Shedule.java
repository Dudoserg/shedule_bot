package com.shedule.shedule_bot.entity;

import com.shedule.shedule_bot.parser.GroupInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Shedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    @ManyToOne
    @JoinColumn(name = "timeSubject_id", nullable = false)
    private TimeSubject timeSubject;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;


    private String subjectType = null;
    private String cabinet = null;

    private Integer week = null;
    private String dayName = null;
    private Integer dayOfWeek = -1;
    private Integer starYear = -1;


    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;


    public void calCulateDayOfWeek(){
        if( this.dayName.equals("Понедельник"))
            this.dayOfWeek = 1;
        else if( this.dayName.equals("Вторник"))
            this.dayOfWeek = 2;
        else if( this.dayName.equals("Среда"))
            this.dayOfWeek = 3;
        else if( this.dayName.equals("Четверг"))
            this.dayOfWeek = 4;
        else if( this.dayName.equals("Пятница"))
            this.dayOfWeek = 5;
        else if( this.dayName.equals("Суббота"))
            this.dayOfWeek = 6;
        else if( this.dayName.equals("Воскресенье"))
            this.dayOfWeek = 7;
    }
}
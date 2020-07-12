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


    //    private String dayName = null;
//    private Integer dayOfWeek = -1;
    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;


    private Integer starYear = -1;


    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;



}
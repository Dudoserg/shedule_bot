package com.shedule.shedule_bot.entity;

import com.shedule.shedule_bot.parser.GroupInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Shedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String time = null;
    private String subject = null;
    private String subjectType = null;
    private String cabinet = null;
    private String teacher = null;
    private String teacherRang = null;
    private Integer week = null;
    private String dayName = null;

    //private GroupInfo groupInfo = null;
    private String groupId;
    private String groupName;
    private String facultId;
    private String facultName;
}
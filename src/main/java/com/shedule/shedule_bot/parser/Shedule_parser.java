package com.shedule.shedule_bot.parser;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Shedule_parser {
    private String time = null;
    private Integer timeStart = null;
    private Integer timeFinish = null;
    private String subject = null;
    private String subject_type = null;
    private String cabinet = null;
    private String teacher = null;
    private String teacher_rang = null;
    private GroupInfo groupInfo = null;
    private Integer week = null;
    private String dayName = null;
}

package com.shedule.shedule_bot.parser;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupInfo {
    private Integer start_year;
    private String name;
    private String faculty_id;
    private String speciality_id;
    private Integer group_br;
    private String id;
    private String facult_name;

    public GroupInfo() {
    }
}

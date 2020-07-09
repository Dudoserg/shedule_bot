package com.shedule.shedule_bot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private Integer startYear;
    private String name;
    private String facultyId;
    private String specialityId;
    private Integer groupBr;
    private String groupId;
    private String facultyName;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

}

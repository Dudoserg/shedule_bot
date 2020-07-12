package com.shedule.shedule_bot.entity.Db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Group_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private Integer startYear;
    private String name;

    private String specialityId;
    private String groupId;
    private Integer groupBr;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;


    @OneToMany( mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Shedule> sheduleSet;

}

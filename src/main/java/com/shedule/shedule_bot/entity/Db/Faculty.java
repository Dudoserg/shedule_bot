package com.shedule.shedule_bot.entity.Db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String facultyId;
    private String facultyName;

    @OneToMany( mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Group> groupSet;
}

package com.shedule.shedule_bot.entity.Db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "teacherRang_id", nullable = false)
    private TeacherRang teacherRang;


    @OneToMany( mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Shedule> sheduleSet;

    public Teacher(String name, TeacherRang teacherRang) {
        this.name = name;
        this.teacherRang = teacherRang;
    }

    public Teacher() {
    }
}

package com.shedule.shedule_bot.entity.Db.Shedule;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Day_table")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String dayName;
    private Integer dayOfWeek;

    public Day(String dayName) {
        this.dayName = dayName;
        this.calCulateDayOfWeek();
    }

    public Day() {
    }

    public void calCulateDayOfWeek() {
        if (this.dayName.equals("Понедельник"))
            this.dayOfWeek = 1;
        else if (this.dayName.equals("Вторник"))
            this.dayOfWeek = 2;
        else if (this.dayName.equals("Среда"))
            this.dayOfWeek = 3;
        else if (this.dayName.equals("Четверг"))
            this.dayOfWeek = 4;
        else if (this.dayName.equals("Пятница"))
            this.dayOfWeek = 5;
        else if (this.dayName.equals("Суббота"))
            this.dayOfWeek = 6;
        else if (this.dayName.equals("Воскресенье"))
            this.dayOfWeek = 7;
    }
}

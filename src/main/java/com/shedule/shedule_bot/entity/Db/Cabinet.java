package com.shedule.shedule_bot.entity.Db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Cabinet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String cabinetTitle;

    public Cabinet() {
    }

    public Cabinet(String cabinetTitle) {
        this.cabinetTitle = cabinetTitle;
    }
}

package com.shedule.shedule_bot.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserTg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String chatId;
    private String firstName;
    private String lastName;
    private String username;

    // В каком состоянии находится бот (вершина конечного автомата)
    private Integer state = -1;
    // В каком состоянии бот находился до последнего действия
    private Integer prev_state = -1;


    // запоминаем выбранный факультет
    @ManyToOne
    @JoinColumn(name = "saveFaculty_1", nullable = true)
    private Faculty saveFaculty_1 = null;

    @ManyToOne
    @JoinColumn(name = "savedGroup_1", nullable = true)
    private Group savedGroup_1 = null;


    public UserTg() {
    }

    public void updateState(Integer state){
        this.prev_state = this.state;
        this.state = state;
    }


}

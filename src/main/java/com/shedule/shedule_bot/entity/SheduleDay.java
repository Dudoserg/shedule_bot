package com.shedule.shedule_bot.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
// список предметов на день
public class SheduleDay {
    private String dayName;
    private Integer dayOfWeek;
    private Integer week;
    private List<Shedule> sheduleList = new ArrayList<>();

    public SheduleDay(String dayName, Integer dayOfWeek, Integer week) {
        this.dayName = dayName;
        this.dayOfWeek = dayOfWeek;
        this.week = week;
    }

    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.bold("Неделя №" + week));
        stringBuilder.append("\n");
        if (sheduleList.size() == 0) {
            stringBuilder.append("\nНа данный день расписания нет\n");
            return stringBuilder.toString();
        }
        stringBuilder.append(this.pre(sheduleList.get(0).getDayName()));
        stringBuilder.append("\n");
        for (Shedule shedule : sheduleList) {
            stringBuilder.append(this.underline(shedule.getTime())).append("  ");
            String subject = shedule.getSubject();
            final String subjectFirstUpper = Arrays.asList(
                    subject.trim().split(" ")
            ).stream()
                    .map(s -> {
                        if (s.length() > 2)
                            return s.substring(0, 1) + s.substring(1).toLowerCase();
                        else
                            return s;
                    })
                    .collect(Collectors.joining(" "));
            stringBuilder.append(subjectFirstUpper).append(" ");
            stringBuilder.append(shedule.getSubjectType()).append(" ");
            stringBuilder.append(shedule.getCabinet()).append(" - ");
            stringBuilder.append(this.italic(shedule.getTeacher())).append(" - ");
            stringBuilder.append(shedule.getTeacherRang());
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String italic(String str) {
        return "<i>" + str + "</i>";
    }

    private String underline(String str) {
        return "<u>" + str + "</u>";
    }

    private String pre(String str) {
        return "<pre>" + str + "</pre>";
    }

    private String bold(String str) {
        return "<b>" + str + "</b>";
    }
}

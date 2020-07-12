package com.shedule.shedule_bot.entity;

import com.shedule.shedule_bot.entity.Db.Day;
import com.shedule.shedule_bot.entity.Db.Shedule;
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
    private Day day;

    private Integer week;
    private List<Shedule> sheduleList = new ArrayList<>();

    public SheduleDay(Day day, Integer week) {
        this.day = day;
        this.week = week;
    }

    private String getString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pre(sheduleList.get(0).getDay().getDayName()));
        stringBuilder.append("\n");
        for (Shedule shedule : sheduleList) {
            stringBuilder.append(underline(shedule.getTimeSubject().getStartToEndStr())).append("  ");
            String subject = shedule.getSubject().getSubjectName();
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
            stringBuilder.append(italic(shedule.getTeacher().getName())).append(" - ");
            stringBuilder.append(shedule.getTeacher().getTeacherRang().getRangName());
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public String getSheduleDayString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bold("Неделя №" + week));
        stringBuilder.append("\n");
        if (sheduleList.size() == 0) {
            if (this.getDay().getDayOfWeek() != 7)
                stringBuilder.append("\nНа данный день расписания нет\n");
            else
                stringBuilder.append("\nВоскресенье, можете отдохнуть =)\n");
            return stringBuilder.toString();
        }
       return stringBuilder.append(this.getString()).toString();
    }


    public static String getSheduleWeekString(List<SheduleDay> list){
        if (list.size() == 0)
            return "На эту неделю нет расписания";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bold("Неделя №" + list.get(0).getWeek()));
        stringBuilder.append("\n");
        for (SheduleDay sheduleDay : list) {
            if(sheduleDay.getSheduleList().size() == 0)
                continue;
            stringBuilder.append(sheduleDay.getString());
        }
        return stringBuilder.toString();
    }

    private static String italic(String str) {
        return "<i>" + str + "</i>";
    }

    private static String underline(String str) {
        return "<u>" + str + "</u>";
    }

    private static String pre(String str) {
        return "<pre>" + str + "</pre>";
    }

    private static String bold(String str) {
        return "<b>" + str + "</b>";
    }
}

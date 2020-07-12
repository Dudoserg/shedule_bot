package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Group;
import com.shedule.shedule_bot.entity.Shedule;
import com.shedule.shedule_bot.entity.SheduleDay;
import com.shedule.shedule_bot.repo.SheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SheduleService {

    @Autowired
    SheduleRepo sheduleRepo;

//    public List<Shedule> findAllByGroupName(String groupName){
//        final List<Shedule> allByGroup_name = sheduleRepo.findAllByGroupName(groupName);
//        return allByGroup_name;
//    }

    public Long getCountRow() {
        return sheduleRepo.getCountRow();
    }

    /**
     * Получить расписание на конкретную дату
     *
     * @param group     группа
     * @param localDate дата
     * @return список предметов
     */
    public SheduleDay getSheduleByDate(Group group, LocalDate localDate) {
        final int dayOfWeek = localDate.getDayOfWeek().getValue();

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = localDate.get(weekFields.weekOfWeekBasedYear());
        weekNumber = (weekNumber % 2) + 1;

        final List<Shedule> list = sheduleRepo.findAllByGroupEqualsAndDayOfWeekEqualsAndWeekEquals(group, dayOfWeek, weekNumber);
        if (list.size() == 0)
            return new SheduleDay("", dayOfWeek, weekNumber);
        final SheduleDay sheduleDay = new SheduleDay(list.get(0).getDayName(), list.get(0).getDayOfWeek(), list.get(0).getWeek());
        sheduleDay.setSheduleList(list);
        return sheduleDay;
    }

    /**
     * Получить расписание группы на первую или вторую неделю
     *
     * @param group   группа
     * @param weekNum номер недели
     * @return список дней
     * @throws Exception неправильный номер недели
     */
    private List<SheduleDay> getSheduleByWeek(Group group, Integer weekNum) throws Exception {
        if (weekNum <= 0 || weekNum > 2) {
            throw new Exception("wrong weeNum (" + weekNum + ")");
        }
        final List<Shedule> list = sheduleRepo.findAllByGroupEqualsAndWeekEquals(group, weekNum);
        List<SheduleDay> sheduleDayList = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            sheduleDayList.add(new SheduleDay("", i + 1, weekNum));
        for (Shedule shedule : list) {
            final SheduleDay sheduleDay = sheduleDayList.get(shedule.getDayOfWeek());
            sheduleDay.setDayName(shedule.getDayName());
            sheduleDay.getSheduleList().add(shedule);
        }
        return sheduleDayList;
    }

    /**
     * Получить расписание группы на сегодня
     *
     * @param group группа
     * @return список предметов
     */
    public SheduleDay getSheduleToday(Group group) {
        return this.getSheduleByDate(group, LocalDate.now());
    }

    /**
     * Получить расписание группы на завтра
     *
     * @param group группа
     * @return список предметов
     */
    public SheduleDay getSheduleTomorrow(Group group) {
        return this.getSheduleByDate(group, LocalDate.now().plusDays(1));
    }

    public List<SheduleDay> getSheduleFirstWeek(Group groupSheduleShow) throws Exception {
        return this.getSheduleByWeek(groupSheduleShow, 1);
    }
    public List<SheduleDay> getSheduleSecondWeek(Group groupSheduleShow) throws Exception {
        return this.getSheduleByWeek(groupSheduleShow, 2);
    }


//    public List<Integer> getSchoolTime(){
//        return sheduleRepo.getDistinctSheduleTime();
//    }
}

package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.TimeSubject;
import com.shedule.shedule_bot.repo.TimeSubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TimeSubjectServiceImpl implements TimeSubjectService{
    final
    TimeSubjectRepo timeSubjectRepo;

    public TimeSubjectServiceImpl(TimeSubjectRepo timeSubjectRepo) {
        this.timeSubjectRepo = timeSubjectRepo;
    }

    public TimeSubject getByStartEnd(Integer timeStart, Integer timeEnd) {
        TimeSubject timeSubject = timeSubjectRepo.findAllByTimeStartEqualsAndTimeEndEquals(timeStart, timeEnd);
        if (timeSubject == null) {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-0"));
            final String timeStartStr = sdf.format(timeStart * 1000);
            final String timeEndStr = sdf.format(timeEnd * 1000);
            timeSubject = new TimeSubject(timeStart, timeEnd, timeStartStr, timeEndStr);
            timeSubject = timeSubjectRepo.save(timeSubject);
        }
        return timeSubject;
    }

    public List<TimeSubject> getAll() {
        return timeSubjectRepo.findAll();
    }
}

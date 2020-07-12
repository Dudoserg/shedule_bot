package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.TeacherRang;
import com.shedule.shedule_bot.repo.TeacherRangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherRangService {
    final
    TeacherRangRepo teacherRangRepo;

    public TeacherRangService(TeacherRangRepo teacherRangRepo) {
        this.teacherRangRepo = teacherRangRepo;
    }


    public TeacherRang getByRangName(String rangName){
         TeacherRang teacherRang = teacherRangRepo.findAllByRangNameEquals(rangName);
        if(teacherRang == null){
            teacherRang = new TeacherRang(rangName);
            teacherRang = teacherRangRepo.save(teacherRang);
        }
        return teacherRang;
    }
}

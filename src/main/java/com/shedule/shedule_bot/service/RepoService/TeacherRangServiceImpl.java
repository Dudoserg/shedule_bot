package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.TeacherRang;
import com.shedule.shedule_bot.repo.TeacherRangRepo;
import org.springframework.stereotype.Service;

@Service
public class TeacherRangServiceImpl  implements TeacherRangService{
    final
    TeacherRangRepo teacherRangRepo;

    public TeacherRangServiceImpl(TeacherRangRepo teacherRangRepo) {
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

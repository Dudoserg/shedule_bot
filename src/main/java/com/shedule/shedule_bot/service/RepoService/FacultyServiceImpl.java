package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Faculty;
import com.shedule.shedule_bot.repo.FacultyRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService{

    final
    FacultyRepo facultyRepo;

    public FacultyServiceImpl(FacultyRepo facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    public List<Faculty> getAllFaculty(){
        return facultyRepo.findAll();
    }

    public Faculty getFacultyByName(String name){
        return facultyRepo.findFacultyByFacultyName(name);
    }
}

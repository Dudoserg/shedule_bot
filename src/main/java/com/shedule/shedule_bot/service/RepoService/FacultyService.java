package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Faculty;
import com.shedule.shedule_bot.repo.FacultyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    final
    FacultyRepo facultyRepo;

    public FacultyService(FacultyRepo facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    public List<Faculty> getAllFaculty(){
        return facultyRepo.findAll();
    }

    public Faculty getFacultyByName(String name){
        return facultyRepo.findFacultyByFacultyName(name);
    }
}

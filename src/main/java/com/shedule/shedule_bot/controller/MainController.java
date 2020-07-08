package com.shedule.shedule_bot.controller;


import com.shedule.shedule_bot.entity.Shedule;
import com.shedule.shedule_bot.service.SheduleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class MainController {

    @Autowired
    SheduleService sheduleService;

    @RequestMapping(value = "/test", method = GET)
    public ResponseEntity<Object> account() throws NotFoundException {
        final List<Shedule> allByGroup_name = sheduleService.findAllByGroupName("8Арх-81");

        allByGroup_name.sort(Comparator.comparing(Shedule::getWeek).thenComparing(Shedule::getDayOfWeek));

        ResponseEntity<Object> responseEntity =
                new ResponseEntity<Object>(allByGroup_name, HttpStatus.OK);

        return responseEntity;
    }
}

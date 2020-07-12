package com.shedule.shedule_bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.parser.ALTGTU.Parse;
import com.shedule.shedule_bot.service.RepoService.SheduleService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ParserController {

    final Parse parse;
    final SheduleService sheduleService;

    public ParserController(Parse parse, SheduleService sheduleService) {
        this.parse = parse;
        this.sheduleService = sheduleService;
    }

    @RequestMapping(value = "/altgtu", method = GET)
    public ResponseEntity<Object> account() throws NotFoundException {
        Long countRow = sheduleService.getCountRow();
        if (sheduleService.getCountRow() > 0)
            return new ResponseEntity<>("class schedule for this institution has already been received", HttpStatus.OK);
        try {
            parse.start();
        } catch (JsonProcessingException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }
}

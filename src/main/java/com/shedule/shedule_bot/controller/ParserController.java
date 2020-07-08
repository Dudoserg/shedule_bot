package com.shedule.shedule_bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.parser.ALTGTU.Parse;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.parser.Parser;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ParserController {

    @Autowired
    Parse parse;

    @RequestMapping(value = "/altgtu", method = GET)
    public ResponseEntity<Object> account() throws NotFoundException {
        try {
            parse.start();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }
}

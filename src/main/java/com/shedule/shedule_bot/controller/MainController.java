package com.shedule.shedule_bot.controller;


import com.shedule.shedule_bot.service.RepoService.SheduleServiceImpl;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    final
    SheduleServiceImpl sheduleService;

    public MainController(SheduleServiceImpl sheduleService) {
        this.sheduleService = sheduleService;
    }

//    @RequestMapping(value = "/test", method = GET)
//    public ResponseEntity<Object> account() throws NotFoundException {
//
//
//        final List<Shedule> allByGroup_name = sheduleService.findAllByGroupName("8Арх-81");
//        allByGroup_name.sort(Comparator.comparing(Shedule::getWeek).thenComparing(Shedule::getDayOfWeek));
//
//        return new ResponseEntity<>(allByGroup_name, HttpStatus.OK);
//    }

}

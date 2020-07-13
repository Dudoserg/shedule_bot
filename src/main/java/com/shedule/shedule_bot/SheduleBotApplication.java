package com.shedule.shedule_bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.parser.ALTGTU.Parse;
import com.shedule.shedule_bot.repo.SheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class SheduleBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SheduleBotApplication.class, args);
    }

}

package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.UserTg;
import com.shedule.shedule_bot.repo.UserTgRepo;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTgService {
    final UserTgRepo userTgRepo;

    public UserTgService(UserTgRepo userTgRepo) {
        this.userTgRepo = userTgRepo;
    }

    public Optional<UserTg> getUserByChatId(String chatId){
        final Optional<UserTg> userTgByChatId = userTgRepo.findUserTgByChatId(chatId);
        return userTgByChatId;
    }
    public UserTg save(UserTg userTg){
        return userTgRepo.save(userTg);
    }
}
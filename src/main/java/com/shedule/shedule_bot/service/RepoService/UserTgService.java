package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.UserTg;

import java.util.Optional;

public interface UserTgService {
    UserTg save(UserTg userTg);
    Optional<UserTg> getUserByChatId(String chatId);
}

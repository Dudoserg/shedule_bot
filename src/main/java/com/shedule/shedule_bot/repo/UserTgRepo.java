package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.UserTg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTgRepo extends JpaRepository<UserTg, Long> {
    Optional<UserTg> findUserTgByChatId(String chatId);
}
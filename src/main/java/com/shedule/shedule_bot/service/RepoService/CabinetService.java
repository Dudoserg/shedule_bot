package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Cabinet;

public interface CabinetService {
    Cabinet getByTitle(String cabinetTitle);
}

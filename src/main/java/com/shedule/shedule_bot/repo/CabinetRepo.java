package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CabinetRepo extends JpaRepository<Cabinet, Long> {
    Cabinet findAllByCabinetTitleEquals(String cabinetTitle);
}

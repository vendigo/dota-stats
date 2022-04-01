package com.github.vendigo.dotastats;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DotaGameRecordRepository extends Repository<DotaGameRecord, Long> {

    @Query("SELECT game FROM DotaGameRecord game WHERE game.date > :date "
            + "ORDER BY game.date DESC")
    List<DotaGameRecord> findAfterDate(@Param("date") LocalDateTime date);
}

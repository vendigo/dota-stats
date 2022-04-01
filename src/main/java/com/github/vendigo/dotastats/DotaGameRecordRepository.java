package com.github.vendigo.dotastats;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.github.vendigo.dotastats.model.DotaGameRecord;

public interface DotaGameRecordRepository extends Repository<DotaGameRecord, Long> {

    @Query("SELECT game FROM DotaGameRecord game WHERE game.date BETWEEN :dateFrom AND :dateTo "
        + "ORDER BY game.date DESC")
    List<DotaGameRecord> findByDateBetween(@Param("dateFrom") LocalDateTime date,
        @Param("dateTo") LocalDateTime dateTo);
}

package com.github.vendigo.dotastats;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;

@RestController
@AllArgsConstructor
public class StatsController {

    private final DotaStatsService dotaStatsService;

    @GetMapping("/dota-stats")
    public DotaStats dotaStats() {
        return dotaStatsService.getStats(LocalDateTime.of(2022, Month.MARCH, 15, 0, 0));
    }
}

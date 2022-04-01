package com.github.vendigo.dotastats;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.vendigo.dotastats.model.DotaStats;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class StatsController {

    private final DotaStatsService dotaStatsService;

    @GetMapping("/dota-stats")
    public List<DotaStats> dotaStats() {
        return dotaStatsService.getStats(
            List.of(
                new StatsPeriod("З від'їзду дівчат", LocalDate.of(2022, Month.MARCH, 15),
                    LocalDate.of(2022, Month.APRIL, 1)),
                new StatsPeriod("З початку року", LocalDate.of(2022, Month.JANUARY, 1),
                    LocalDate.of(2022, Month.APRIL, 1)),
                new StatsPeriod("Від покупки компа", LocalDate.of(2021, Month.NOVEMBER, 6),
                    LocalDate.of(2022, Month.APRIL, 1)),
                new StatsPeriod("Від народження доньки до від'їзду", LocalDate.of(2021, Month.APRIL, 1),
                    LocalDate.of(2022, Month.MARCH, 14)),
                new StatsPeriod("Playkey", LocalDate.of(2021, Month.FEBRUARY, 1),
                    LocalDate.of(2021, Month.NOVEMBER, 5)),
                new StatsPeriod("2020 рік", LocalDate.of(2020, Month.JANUARY, 1),
                    LocalDate.of(2020, Month.DECEMBER, 31))
            )
        );
    }
}

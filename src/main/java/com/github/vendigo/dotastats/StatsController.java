package com.github.vendigo.dotastats;

import com.github.vendigo.dotastats.model.DotaStats;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {

    private final DotaStatsService dotaStatsService;

    @GetMapping("/dota-stats")
    public List<DotaStats> dotaStats() {
        return dotaStatsService.getStats(
                List.of(
                        new StatsPeriod("Після повернення до доти", LocalDate.of(2022, Month.AUGUST, 12),
                                LocalDate.of(2022, Month.AUGUST, 31)),
                        new StatsPeriod("З від'їзду дівчат", LocalDate.of(2022, Month.MARCH, 15),
                                LocalDate.now()),
                        new StatsPeriod("З початку року", LocalDate.of(2022, Month.JANUARY, 1),
                                LocalDate.now()),
                        new StatsPeriod("Від покупки компа", LocalDate.of(2021, Month.NOVEMBER, 6),
                                LocalDate.now()),
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

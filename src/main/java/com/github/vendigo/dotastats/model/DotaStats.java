package com.github.vendigo.dotastats.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DotaStats {

    private String periodName;
    private int totalGames;
    private double totalHoursPlayed;
    private int daysInPeriod;
    private String period;
    private String results;
    private String winRate;
    private int mmrChange;
    private int daysPlayed;
    private double avgGamesPerDay;
    private double avgHoursPerDay;
    private double avgGamesPerPlayedDay;
    private double avgHoursPerPlayedDay;
    private int maxGamesPerDay;
    private double maxHoursPerDay;
    private List<HeroStats> heroStats;
}

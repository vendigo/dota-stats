package com.github.vendigo.dotastats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DotaStats {
    private int totalGames;
    private double totalHoursPlayed;
    private int daysInPeriod;
    private String results;
    private double winRate;
    private int mmrChange;
    private int daysPlayed;
    private double avgGamesPerDay;
    private double avgHoursPerDay;
    private double avgGamesPerPlayedDay;
    private double avgHoursPerPlayedDay;
    private int maxGamesPerDay;
    private double maxHoursPerDay;
    private HeroStats mostPopularHero;
    private HeroStats mostSuccessfulHero;
}

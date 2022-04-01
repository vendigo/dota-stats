package com.github.vendigo.dotastats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DotaStatsService {

    private final DotaGameRecordRepository gameRecordRepository;

    public DotaStats getStats(LocalDateTime dateFrom) {
        List<DotaGameRecord> games = gameRecordRepository.findAfterDate(dateFrom);

        DotaStats stats = new DotaStats();

        totalStats(games, stats);
        periodStats(games, stats, dateFrom);
        avgStats(stats);
        maxStats(games, stats);

        return stats;
    }

    private void totalStats(List<DotaGameRecord> games, DotaStats stats) {
        int totalGames = games.size();
        double totalHours = games.stream()
                .mapToInt(DotaGameRecord::getDuration)
                .sum() / 60.0;

        int wins = (int) games.stream()
                .map(DotaGameRecord::getResult)
                .filter("Won"::equals)
                .count();
        int loses = totalGames - wins;
        String results = wins + " / " + loses;
        double winRate = (double) wins / (wins + loses);
        int mmrChange = (wins - loses) * 30;

        stats.setTotalGames(totalGames);
        stats.setTotalHoursPlayed(totalHours);
        stats.setResults(results);
        stats.setWinRate(winRate);
        stats.setMmrChange(mmrChange);
    }

    private void periodStats(List<DotaGameRecord> games, DotaStats stats, LocalDateTime dateFrom) {
        LocalDate startDate = dateFrom.toLocalDate();
        LocalDate endDate = games.stream()
                .map(DotaGameRecord::getDate)
                .map(LocalDateTime::toLocalDate)
                .findFirst()
                .orElseGet(LocalDate::now);
        Period period = Period.between(startDate, endDate.plusDays(1));
        int daysInPeriod = period.getDays();

        int daysPlayed = (int) games.stream()
                .map(DotaGameRecord::getDate)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .count();

        stats.setDaysPlayed(daysPlayed);
        stats.setDaysInPeriod(daysInPeriod);
    }

    private void avgStats(DotaStats stats) {
        int daysInPeriod = stats.getDaysInPeriod();
        double avgGamesPerDay = (double) stats.getTotalGames() / daysInPeriod;
        double avgHoursPerDay = stats.getTotalHoursPlayed() / daysInPeriod;

        int daysPlayed = stats.getDaysPlayed();
        double avgGamesPerPlayedDay = (double) stats.getTotalGames() / daysPlayed;
        double avgHoursPerPlayedDay = stats.getTotalHoursPlayed() / daysPlayed;

        stats.setAvgGamesPerDay(avgGamesPerDay);
        stats.setAvgHoursPerDay(avgHoursPerDay);
        stats.setAvgGamesPerPlayedDay(avgGamesPerPlayedDay);
        stats.setAvgHoursPerPlayedDay(avgHoursPerPlayedDay);
    }

    private void maxStats(List<DotaGameRecord> games, DotaStats stats) {
        Map<LocalDate, List<DotaGameRecord>> gamesByDay = games.stream()
                .collect(Collectors.groupingBy(game -> game.getDate().toLocalDate()));
        int maxGamesPerDay = gamesByDay.values().stream()
                .mapToInt(Collection::size)
                .max()
                .orElse(0);
        double maxHoursPerDay = gamesByDay.values().stream()
                .mapToInt(dayGames -> dayGames.stream().mapToInt(DotaGameRecord::getDuration).sum())
                .max()
                .orElse(0) / 60.0;

        stats.setMaxGamesPerDay(maxGamesPerDay);
        stats.setMaxHoursPerDay(maxHoursPerDay);
    }
}

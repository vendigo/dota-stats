package com.github.vendigo.dotastats;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.vendigo.dotastats.model.DotaGameRecord;
import com.github.vendigo.dotastats.model.DotaStats;
import com.github.vendigo.dotastats.model.HeroStats;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DotaStatsService {

    private static final Comparator<List<DotaGameRecord>> MORE_GAMES = Comparator
        .<List<DotaGameRecord>, Integer>comparing(List::size)
        .reversed();

    private final DotaGameRecordRepository gameRecordRepository;

    public List<DotaStats> getStats(List<StatsPeriod> periods) {
        return periods.stream()
            .map(this::getStats)
            .toList();
    }

    private DotaStats getStats(StatsPeriod period) {
        LocalDateTime dateFrom = period.dateFrom().atStartOfDay();
        List<DotaGameRecord> games = gameRecordRepository.findByDateBetween(dateFrom,
            period.dateTo().plusDays(1).atStartOfDay());

        DotaStats stats = new DotaStats();
        stats.setPeriodName(period.periodName());

        totalStats(games, stats);
        periodStats(games, stats, dateFrom);
        avgStats(stats);
        maxStats(games, stats);
        heroStats(games, stats, 5);

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
        String winRate = Math.round((double) wins / (wins + loses) * 100) + "%";
        int mmrChange = (wins - loses) * 30;

        stats.setTotalGames(totalGames);
        stats.setTotalHoursPlayed(totalHours);
        stats.setResults(results);
        stats.setWinRate(winRate);
        stats.setMmrChange(mmrChange);
    }

    private void periodStats(List<DotaGameRecord> games, DotaStats stats, LocalDateTime dateFrom) {
        LocalDate startDate = games.stream()
            .map(DotaGameRecord::getDate)
            .map(LocalDateTime::toLocalDate)
            .sorted()
            .findFirst()
            .orElseGet(dateFrom::toLocalDate);
        LocalDate endDate = games.stream()
            .map(DotaGameRecord::getDate)
            .map(LocalDateTime::toLocalDate)
            .findFirst()
            .orElseGet(LocalDate::now);
        String playingPeriod = startDate + " - " + endDate;
        int daysInPeriod = (int) ChronoUnit.DAYS.between(startDate, endDate);

        int daysPlayed = (int) games.stream()
            .map(DotaGameRecord::getDate)
            .map(LocalDateTime::toLocalDate)
            .distinct()
            .count();

        stats.setDaysPlayed(daysPlayed);
        stats.setDaysInPeriod(daysInPeriod);
        stats.setPeriod(playingPeriod);
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

    private void heroStats(List<DotaGameRecord> games, DotaStats stats, int topK) {
        Map<String, List<DotaGameRecord>> gamesByHero = games.stream()
            .collect(Collectors.groupingBy(DotaGameRecord::getHero));
        List<HeroStats> heroStats = gamesByHero.values().stream()
            .sorted(MORE_GAMES)
            .limit(topK)
            .map(this::heroStats)
            .toList();
        stats.setHeroStats(heroStats);
    }

    private HeroStats heroStats(List<DotaGameRecord> heroGames) {
        DotaGameRecord firstGame = heroGames.get(0);
        int totalGames = heroGames.size();
        int wins = (int) heroGames.stream()
            .filter(game -> "Won".equals(game.getResult()))
            .count();
        int loses = totalGames - wins;
        String results = wins + " / " + loses;
        String winRate = Math.round((double) wins / (wins + loses) * 100) + "%";

        return new HeroStats(firstGame.getHero(), totalGames, winRate, results);
    }
}

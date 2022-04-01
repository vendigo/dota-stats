package com.github.vendigo.dotastats;

import java.time.LocalDate;

public record StatsPeriod(String periodName, LocalDate dateFrom, LocalDate dateTo) {

}

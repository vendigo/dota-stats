package com.github.vendigo.dotastats.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "dota_game_records")
@Getter
@Setter
public class DotaGameRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "hero", nullable = false, length = 50)
    private String hero;

    @Column(name = "result", nullable = false, length = 50)
    private String result;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "kills", nullable = false)
    private Integer kills;

    @Column(name = "deaths", nullable = false)
    private Integer deaths;

    @Column(name = "assists", nullable = false)
    private Integer assists;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

}
package br.com.omnisightAnalytics.dto;

public record PlayerStatsDTO(
        String playerId,
        int totalKills,
        int totalDeaths,
        double kdRatio
) {
}

package br.com.omnisightAnalytics.dto;

import br.com.omnisightAnalytics.dto.PlayerStatsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerStatsDTOTest {

    @Test
    void shouldCreateRecordAndAccessFields() {
        PlayerStatsDTO dto = new PlayerStatsDTO("player-999", 120, 40, 3.0);

        assertEquals("player-999", dto.playerId());
        assertEquals(120, dto.totalKills());
        assertEquals(40, dto.totalDeaths());
        assertEquals(3.0, dto.kdRatio());
    }
}
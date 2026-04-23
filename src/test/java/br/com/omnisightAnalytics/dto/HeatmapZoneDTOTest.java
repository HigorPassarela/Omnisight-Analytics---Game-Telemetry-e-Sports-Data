package br.com.omnisightAnalytics.dto;

import br.com.omnisightAnalytics.dto.HeatmapZoneDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeatmapZoneDTOTest {

    @Test
    void shouldCreateRecordAndAccessFields() {
        HeatmapZoneDTO dto = new HeatmapZoneDTO(150, 300, 85);

        assertEquals(150, dto.gridX());
        assertEquals(300, dto.gridY());
        assertEquals(85, dto.deathCount());
    }
}
package br.com.omnisightAnalytics.dto;

import br.com.omnisightAnalytics.dto.DashboardFacetedDTO;
import br.com.omnisightAnalytics.dto.EventTypeStatDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboardFacetedDTOTest {

    @Test
    void shouldCreateRecordAndAccessFields() {
        List<EventTypeStatDTO> events = List.of(
                new EventTypeStatDTO("kill", 150),
                new EventTypeStatDTO("death", 100)
        );

        DashboardFacetedDTO dto = new DashboardFacetedDTO(events, 20, 3500);

        assertEquals(events, dto.eventsDistribution());
        assertEquals(20, dto.peakHour());
        assertEquals(3500, dto.peakHourEventCount());
    }
}
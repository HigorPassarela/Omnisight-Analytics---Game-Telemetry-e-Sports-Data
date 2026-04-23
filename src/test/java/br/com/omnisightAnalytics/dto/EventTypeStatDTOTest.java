package br.com.omnisightAnalytics.dto;

import br.com.omnisightAnalytics.dto.EventTypeStatDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventTypeStatDTOTest {

    @Test
    void shouldCreateRecordAndAccessFields() {
        EventTypeStatDTO dto = new EventTypeStatDTO("assist", 45);

        assertEquals("assist", dto.eventType());
        assertEquals(45, dto.count());
    }
}
package br.com.omnisightAnalytics.domain;

import br.com.omnisightAnalytics.domain.Coordinates;
import br.com.omnisightAnalytics.domain.MatchEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MatchEventTest {

    @Test
    void shouldCreateMatchEventWithDefaultConstructorAndSetters() {
        MatchEvent event = new MatchEvent();
        assertNull(event.getId());
        assertNull(event.getMatchId());
        assertNull(event.getPlayerId());
        assertNull(event.getEventType());
        assertNull(event.getTimestamp());
        assertNull(event.getCoordinates());

        Instant now = Instant.now();
        Coordinates coords = new Coordinates(10.0, 20.0);

        event.setId("123");
        event.setMatchId("match-1");
        event.setPlayerId("player-1");
        event.setEventType("kill");
        event.setTimestamp(now);
        event.setCoordinates(coords);

        assertEquals("123", event.getId());
        assertEquals("match-1", event.getMatchId());
        assertEquals("player-1", event.getPlayerId());
        assertEquals("kill", event.getEventType());
        assertEquals(now, event.getTimestamp());
        assertEquals(coords, event.getCoordinates());
    }

    @Test
    void shouldCreateMatchEventWithParameterizedConstructor() {
        Instant now = Instant.now();
        Coordinates coords = new Coordinates(50.0, 60.0);

        MatchEvent event = new MatchEvent("match-2", "player-2", "death", now, coords);

        assertNull(event.getId());
        assertEquals("match-2", event.getMatchId());
        assertEquals("player-2", event.getPlayerId());
        assertEquals("death", event.getEventType());
        assertEquals(now, event.getTimestamp());
        assertEquals(coords, event.getCoordinates());
    }
}
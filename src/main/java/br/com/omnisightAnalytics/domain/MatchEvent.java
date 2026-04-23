package br.com.omnisightAnalytics.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "match_events")
@CompoundIndex(name = "event_player_idx", def = "{'event_type': 1, 'player_id': 1}")
public class MatchEvent {

    @Id
    private String id;

    @Field("match_id")
    private String matchId;

    @Field("player_id")
    private String playerId;

    @Field("event_type")
    private String eventType;

    @Field("timestamp")
    private Instant timestamp;

    private Coordinates coordinates;

    public MatchEvent() {
    }

    public MatchEvent(String matchId, String playerId, String eventType, Instant timestamp, Coordinates coordinates) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}

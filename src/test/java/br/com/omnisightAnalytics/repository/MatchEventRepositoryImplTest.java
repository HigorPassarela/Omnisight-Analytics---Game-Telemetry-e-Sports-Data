package br.com.omnisightAnalytics.repository;

import br.com.omnisightAnalytics.dto.DashboardFacetedDTO;
import br.com.omnisightAnalytics.dto.HeatmapZoneDTO;
import br.com.omnisightAnalytics.dto.PlayerStatsDTO;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchEventRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private MatchEventRepositoryImpl repository;

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnTopPlayersByKdRatio() {
        AggregationResults<PlayerStatsDTO> aggregationResults = mock(AggregationResults.class);
        List<PlayerStatsDTO> expectedList = List.of(new PlayerStatsDTO("player-1", 20, 5, 4.0));

        when(aggregationResults.getMappedResults()).thenReturn(expectedList);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("match_events"), eq(PlayerStatsDTO.class)))
                .thenReturn(aggregationResults);

        List<PlayerStatsDTO> result = repository.findTopPlayersByKdRatio(10, 5);

        assertEquals(1, result.size());
        assertEquals("player-1", result.get(0).playerId());
        assertEquals(4.0, result.get(0).kdRatio());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnDeathHeatMap() {
        AggregationResults<HeatmapZoneDTO> aggregationResults = mock(AggregationResults.class);
        List<HeatmapZoneDTO> expectedList = List.of(new HeatmapZoneDTO(100, 200, 50));

        when(aggregationResults.getMappedResults()).thenReturn(expectedList);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("match_events"), eq(HeatmapZoneDTO.class)))
                .thenReturn(aggregationResults);

        List<HeatmapZoneDTO> result = repository.findDeathHeatMap(50, 10);

        assertEquals(1, result.size());
        assertEquals(100, result.get(0).gridX());
        assertEquals(200, result.get(0).gridY());
        assertEquals(50, result.get(0).deathCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnDashboardStatsWhenResultIsNull() {
        AggregationResults<Document> aggregationResults = mock(AggregationResults.class);

        when(aggregationResults.getUniqueMappedResult()).thenReturn(null);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("match_events"), eq(Document.class)))
                .thenReturn(aggregationResults);

        DashboardFacetedDTO result = repository.getDashboardStats();

        assertTrue(result.eventsDistribution().isEmpty());
        assertEquals(0, result.peakHour());
        assertEquals(0, result.peakHourEventCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnDashboardStatsWhenResultIsValid() {
        Document mockRawResult = new Document();

        Document eventDistDoc = new Document("eventType", "kill").append("count", 150);
        mockRawResult.put("eventsDistribution", List.of(eventDistDoc));

        Document peakHourDoc = new Document("_id", 19).append("eventCount", 3500);
        mockRawResult.put("peakHourStats", List.of(peakHourDoc));

        AggregationResults<Document> aggregationResults = mock(AggregationResults.class);

        when(aggregationResults.getUniqueMappedResult()).thenReturn(mockRawResult);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("match_events"), eq(Document.class)))
                .thenReturn(aggregationResults);

        DashboardFacetedDTO result = repository.getDashboardStats();

        assertEquals(1, result.eventsDistribution().size());
        assertEquals("kill", result.eventsDistribution().get(0).eventType());
        assertEquals(150, result.eventsDistribution().get(0).count());
        assertEquals(19, result.peakHour());
        assertEquals(3500, result.peakHourEventCount());
    }
}
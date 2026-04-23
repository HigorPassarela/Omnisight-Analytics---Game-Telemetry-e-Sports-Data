package br.com.omnisightAnalytics.repository;

import br.com.omnisightAnalytics.dto.DashboardFacetedDTO;
import br.com.omnisightAnalytics.dto.EventTypeStatDTO;
import br.com.omnisightAnalytics.dto.HeatmapZoneDTO;
import br.com.omnisightAnalytics.dto.PlayerStatsDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchEventRepositoryImpl implements MatchEventRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public MatchEventRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PlayerStatsDTO> findTopPlayersByKdRatio(int minKills, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(

                Aggregation.match(Criteria.where("event_type").in("kill", "death")),

                Aggregation.group("player_id")
                        .sum(ConditionalOperators.when(Criteria.where("event_type").is("kill")).then(1).otherwise(0)).as("totalKills")
                        .sum(ConditionalOperators.when(Criteria.where("event_type").is("death")).then(1).otherwise(0)).as("totalDeaths"),

                Aggregation.match(Criteria.where("totalKills").gte(minKills)),

                Aggregation.project("totalKills", "totalDeaths")
                        .and("_id").as("playerId")
                        .and(
                                ConditionalOperators.when(Criteria.where("totalDeaths").is(0))
                                        .thenValueOf("totalKills")
                                        .otherwiseValueOf(ArithmeticOperators.valueOf("totalKills").divideBy("totalDeaths"))
                        ).as("kdRatio"),

                Aggregation.sort(Sort.Direction.DESC, "kdRatio"),

                Aggregation.limit(limit)
        );

        AggregationResults<PlayerStatsDTO> results = mongoTemplate.aggregate(aggregation, "match_events", PlayerStatsDTO.class);
        return results.getMappedResults();
    }

    @Override
    public List<HeatmapZoneDTO> findDeathHeatMap(int gridSize, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("event_type").is("death")),

                Aggregation.project()
                        .and(ArithmeticOperators.valueOf("coordinates.x")
                                .subtract(ArithmeticOperators.valueOf("coordinates.x").mod(gridSize))).as("gridX")
                        .and(ArithmeticOperators.valueOf("coordinates.y")
                                .subtract(ArithmeticOperators.valueOf("coordinates.y").mod(gridSize))).as("gridY"),

                Aggregation.group("gridX", "gridY")
                        .count().as("deathCount"),

                Aggregation.project("deathCount")
                        .and("_id.gridX").as("gridX")
                        .and("_id.gridY").as("gridY"),

                Aggregation.sort(Sort.Direction.DESC, "deathCount"),
                Aggregation.limit(limit)
        );

        return mongoTemplate.aggregate(aggregation, "match_events", HeatmapZoneDTO.class).getMappedResults();
    }

    @Override
    public DashboardFacetedDTO getDashboardStats() {
        AggregationOperation facet1 = Aggregation.group("event_type").count().as("count");

        AggregationOperation facet2_project = Aggregation.project()
                .and(org.springframework.data.mongodb.core.aggregation.DateOperators.Hour.hourOf("timestamp")).as("hourOfDay");
        AggregationOperation facet2_group = Aggregation.group("hourOfDay").count().as("eventCount");
        AggregationOperation facet2_sort = Aggregation.sort(Sort.Direction.DESC, "eventCount");
        AggregationOperation facet2_limit = Aggregation.limit(1);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.facet()
                        .and(facet1, Aggregation.project("count").and("_id").as("eventType")).as("eventsDistribution")
                        .and(facet2_project, facet2_group, facet2_sort, facet2_limit).as("peakHourStats")
        );

        org.bson.Document rawResult = mongoTemplate.aggregate(aggregation, "match_events", org.bson.Document.class).getUniqueMappedResult();

        if (rawResult == null) {
            return new DashboardFacetedDTO(java.util.List.of(), 0, 0);
        }

        List<org.bson.Document> distDocs = rawResult.getList("eventsDistribution", org.bson.Document.class);
        List<EventTypeStatDTO> distList = distDocs.stream()
                .map(doc -> new EventTypeStatDTO(doc.getString("eventType"), doc.getInteger("count")))
                .toList();

        int peakHour = 0;
        int peakHourEventCount = 0;
        List<org.bson.Document> peakDocs = rawResult.getList("peakHourStats", org.bson.Document.class);

        if (!peakDocs.isEmpty()) {
            org.bson.Document peakDoc = peakDocs.get(0);
            peakHour = peakDoc.getInteger("_id");
            peakHourEventCount = peakDoc.getInteger("eventCount");
        }

        return new DashboardFacetedDTO(distList, peakHour, peakHourEventCount);
    }
}

package br.com.omnisightAnalytics.controller;

import br.com.omnisightAnalytics.dto.DashboardFacetedDTO;
import br.com.omnisightAnalytics.dto.HeatmapZoneDTO;
import br.com.omnisightAnalytics.dto.PlayerStatsDTO;
import br.com.omnisightAnalytics.repository.MatchEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
@Tag(name = "Players Statistics", description = "Endpoints for analisys players datas")
public class PlayerStatsController {

    private static final Logger log = LoggerFactory.getLogger(PlayerStatsController.class);
    private final MatchEventRepository repository;

    public PlayerStatsController(MatchEventRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/top-players")
    @Operation(summary = "Search top players", description = "Calculate K/D Ration and return the best players")
    public ResponseEntity<List<PlayerStatsDTO>> getTopPlayers(
            @RequestParam(defaultValue = "5") int minKills,
            @RequestParam(defaultValue = "10") int limit) {

        log.info("Search top {} players with minimal {} kills...", limit, minKills);

        long startTime = System.currentTimeMillis();
        List<PlayerStatsDTO> topPlayers = repository.findTopPlayersByKdRatio(minKills, limit);
        long endTime = System.currentTimeMillis();

        log.info("Query executing in {} ms", (endTime - startTime));

        return ResponseEntity.ok(topPlayers);
    }

    @GetMapping("/heatmap")
    @Operation(summary = "Heatmap deaths")
    public ResponseEntity<List<HeatmapZoneDTO>> getHeatmap(
            @RequestParam(defaultValue = "50") int gridSize,
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("Generate heatmap with {} size...", gridSize);
        long startTime = System.currentTimeMillis();

        List<HeatmapZoneDTO> heatmap = repository.findDeathHeatMap(gridSize, limit);

        log.info("Heatmap 1uery executing in {} ms", (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok(heatmap);
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard facet")
    public ResponseEntity<DashboardFacetedDTO> getDashboard() {

        log.info("Generating dashboard with $facet...");
        long startTime = System.currentTimeMillis();

        DashboardFacetedDTO dashboard = repository.getDashboardStats();

        log.info("Dashboard query (facet) executing in {} ms", (System.currentTimeMillis() - startTime));
        return ResponseEntity.ok(dashboard);
    }
}

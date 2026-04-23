package br.com.omnisightAnalytics.repository;

import br.com.omnisightAnalytics.dto.DashboardFacetedDTO;
import br.com.omnisightAnalytics.dto.HeatmapZoneDTO;
import br.com.omnisightAnalytics.dto.PlayerStatsDTO;

import java.util.List;

public interface MatchEventRepositoryCustom {
    List<PlayerStatsDTO> findTopPlayersByKdRatio(int minKills, int limit);
    List<HeatmapZoneDTO> findDeathHeatMap(int gridSize, int limit);
    DashboardFacetedDTO getDashboardStats();
}

package br.com.omnisightAnalytics.controller;

import br.com.omnisightAnalytics.controller.PlayerStatsController;
import br.com.omnisightAnalytics.repository.MatchEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerStatsController.class)
class PlayerStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchEventRepository repository;

    @Test
    void shouldReturnTopPlayersWithDefaultParams() throws Exception {
        when(repository.findTopPlayersByKdRatio(5, 10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stats/top-players"))
                .andExpect(status().isOk());

        verify(repository).findTopPlayersByKdRatio(5, 10);
    }

    @Test
    void shouldReturnTopPlayersWithCustomParams() throws Exception {
        when(repository.findTopPlayersByKdRatio(15, 20)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stats/top-players")
                        .param("minKills", "15")
                        .param("limit", "20"))
                .andExpect(status().isOk());

        verify(repository).findTopPlayersByKdRatio(15, 20);
    }

    @Test
    void shouldReturnHeatmapWithDefaultParams() throws Exception {
        when(repository.findDeathHeatMap(50, 10)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stats/heatmap"))
                .andExpect(status().isOk());

        verify(repository).findDeathHeatMap(50, 10);
    }

    @Test
    void shouldReturnHeatmapWithCustomParams() throws Exception {
        when(repository.findDeathHeatMap(100, 5)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stats/heatmap")
                        .param("gridSize", "100")
                        .param("limit", "5"))
                .andExpect(status().isOk());

        verify(repository).findDeathHeatMap(100, 5);
    }

    @Test
    void shouldReturnDashboard() throws Exception {
        when(repository.getDashboardStats()).thenReturn(null);

        mockMvc.perform(get("/api/v1/stats/dashboard"))
                .andExpect(status().isOk());

        verify(repository).getDashboardStats();
    }
}
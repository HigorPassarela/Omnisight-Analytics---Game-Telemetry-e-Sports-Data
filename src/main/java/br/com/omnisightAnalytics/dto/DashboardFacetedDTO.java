package br.com.omnisightAnalytics.dto;

import java.util.List;

public record DashboardFacetedDTO(
        List<EventTypeStatDTO> eventsDistribution,
        int peakHour,
        int peakHourEventCount
) {
}

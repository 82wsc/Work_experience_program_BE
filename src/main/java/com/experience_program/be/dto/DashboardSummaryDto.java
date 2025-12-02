package com.experience_program.be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DashboardSummaryDto {
    private long totalCampaigns;
    private Map<String, Long> statusCounts;
    private long totalKnowledge;
}

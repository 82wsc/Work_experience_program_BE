package com.experience_program.be.service;

import com.experience_program.be.dto.CountDto;
import com.experience_program.be.entity.Campaign;
import com.experience_program.be.dto.DashboardSummaryDto;
import com.experience_program.be.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final CampaignRepository campaignRepository;
    private final WebClient webClient;

    @Autowired
    public DashboardService(CampaignRepository campaignRepository, WebClient webClient) {
        this.campaignRepository = campaignRepository;
        this.webClient = webClient;
    }

    public Mono<DashboardSummaryDto> getDashboardSummary() {
        List<Object[]> results = campaignRepository.countCampaignsByStatus();

        Map<String, Long> rawCounts = results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        Map<String, Long> finalStatusCounts = new HashMap<>();

        // 1. CREATING (생성중)
        long creatingCount = rawCounts.getOrDefault("PROCESSING", 0L) + rawCounts.getOrDefault("REFINING", 0L);
        finalStatusCounts.put("CREATING", creatingCount);

        // 2. ONGOING (진행중)
        long ongoingCount = rawCounts.getOrDefault("COMPLETED", 0L) + rawCounts.getOrDefault("MESSAGE_SELECTED", 0L);
        finalStatusCounts.put("ONGOING", ongoingCount);

        // 3. CASE_REGISTERED (사례 등록)
        long caseRegisteredCount = rawCounts.getOrDefault("PERFORMANCE_REGISTERED", 0L) + rawCounts.getOrDefault("SUCCESS_CASE", 0L);
        finalStatusCounts.put("CASE_REGISTERED", caseRegisteredCount);

        // 4. DB_REGISTERED (DB 등록)
        long dbRegisteredCount = rawCounts.getOrDefault("RAG_REGISTERED", 0L);
        finalStatusCounts.put("DB_REGISTERED", dbRegisteredCount);

        // 전체 캠페인 수 계산 (FAILED 제외)
        long totalCampaigns = creatingCount + ongoingCount + caseRegisteredCount + dbRegisteredCount;

        // AI 서버에서 totalKnowledge 개수 가져오기
        return webClient.get()
                .uri("/api/knowledge/count")
                .retrieve()
                .bodyToMono(CountDto.class)
                .map(countDto -> new DashboardSummaryDto(totalCampaigns, finalStatusCounts, countDto.getCount()))
                .defaultIfEmpty(new DashboardSummaryDto(totalCampaigns, finalStatusCounts, 0L))
                .onErrorResume(e -> {
                    System.err.println("Error fetching knowledge count from AI server: " + e.getMessage());
                    return Mono.just(new DashboardSummaryDto(totalCampaigns, finalStatusCounts, 0L));
                });
    }

    public List<Campaign> getRecentActivity() {
        return campaignRepository.findTop5ByOrderByRequestDateDesc();
    }
}

package com.experience_program.be.service;

import com.experience_program.be.dto.CountDto;
import com.experience_program.be.entity.Campaign;
import com.experience_program.be.dto.DashboardSummaryDto;
import com.experience_program.be.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    private final CampaignRepository campaignRepository;
    private final WebClient webClient; // WebClient 주입

    @Autowired
    public DashboardService(CampaignRepository campaignRepository, WebClient webClient) {
        this.campaignRepository = campaignRepository;
        this.webClient = webClient;
    }

    public Mono<DashboardSummaryDto> getDashboardSummary() {
        List<String> ongoingStatuses = Arrays.asList("PROCESSING", "REFINING", "MESSAGE_SELECTED");
        long ongoingCampaigns = campaignRepository.countByStatusIn(ongoingStatuses);
        long successCases = campaignRepository.countByIsSuccessCase(true);

        // AI 서버의 /api/knowledge/count 엔드포인트를 호출하여 총 지식 개수를 가져옵니다.
        return webClient.get()
                .uri("/api/knowledge/count")
                .retrieve()
                .bodyToMono(CountDto.class)
                .map(countDto -> new DashboardSummaryDto(ongoingCampaigns, successCases, countDto.getCount()))
                .defaultIfEmpty(new DashboardSummaryDto(ongoingCampaigns, successCases, 0L)) // AI 서버 응답이 없으면 0으로 처리
                .onErrorResume(e -> {
                    System.err.println("Error fetching knowledge count from AI server: " + e.getMessage());
                    return Mono.just(new DashboardSummaryDto(ongoingCampaigns, successCases, 0L)); // 오류 발생 시 0으로 처리
                });
    }

    public List<Campaign> getRecentActivity() {
        return campaignRepository.findTop5ByOrderByRequestDateDesc();
    }
}

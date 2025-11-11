package com.experience_program.be.service;

import com.experience_program.be.dto.KnowledgeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeService {

    private final WebClient webClient;

    @Autowired
    public KnowledgeService(WebClient webClient) {
        this.webClient = webClient;
    }

    // 지식 생성 (AI 서버에 요청)
    public Mono<Object> createKnowledge(KnowledgeDto knowledgeDto) {
        return webClient.post()
                .uri("/api/knowledge")
                .body(Mono.just(knowledgeDto), KnowledgeDto.class)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // 모든 지식 조회 (AI 서버에서 조회)
    public Flux<Object> findAllKnowledge() {
        return webClient.get()
                .uri("/api/knowledge")
                .retrieve()
                .bodyToFlux(Object.class);
    }

    // 특정 지식 조회 (AI 서버에서 조회)
    public Mono<Object> findKnowledgeById(String knowledgeId) {
        return webClient.get()
                .uri("/api/knowledge/" + knowledgeId)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // 지식 수정 (AI 서버에 요청)
    public Mono<Object> updateKnowledge(String knowledgeId, KnowledgeDto knowledgeDto) {
        return webClient.put()
                .uri("/api/knowledge/" + knowledgeId)
                .body(Mono.just(knowledgeDto), KnowledgeDto.class)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // 지식 삭제 (AI 서버에 요청)
    public Mono<Void> deleteKnowledge(String knowledgeId) {
        return webClient.delete()
                .uri("/api/knowledge/" + knowledgeId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

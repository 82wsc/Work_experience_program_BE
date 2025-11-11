package com.experience_program.be.controller;

import com.experience_program.be.dto.KnowledgeDto;
import com.experience_program.be.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @Autowired
    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    // 지식 등록 (정책/팩트)
    @PostMapping
    public Mono<ResponseEntity<Object>> createKnowledge(@RequestBody KnowledgeDto knowledgeDto) {
        return knowledgeService.createKnowledge(knowledgeDto)
                .map(ResponseEntity::ok);
    }

    // 지식 목록 조회
    @GetMapping
    public Flux<Object> getAllKnowledge() {
        return knowledgeService.findAllKnowledge();
    }

    // 지식 상세 조회
    @GetMapping("/{knowledge_id}")
    public Mono<ResponseEntity<Object>> getKnowledgeById(@PathVariable("knowledge_id") String knowledgeId) {
        return knowledgeService.findKnowledgeById(knowledgeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // 지식 수정
    @PutMapping("/{knowledge_id}")
    public Mono<ResponseEntity<Object>> updateKnowledge(@PathVariable("knowledge_id") String knowledgeId, @RequestBody KnowledgeDto knowledgeDto) {
        return knowledgeService.updateKnowledge(knowledgeId, knowledgeDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // 지식 삭제
    @DeleteMapping("/{knowledge_id}")
    public Mono<ResponseEntity<Void>> deleteKnowledge(@PathVariable("knowledge_id") String knowledgeId) {
        return knowledgeService.deleteKnowledge(knowledgeId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

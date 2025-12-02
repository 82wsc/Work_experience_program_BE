package com.experience_program.be.repository;

import com.experience_program.be.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID>, JpaSpecificationExecutor<Campaign> {

    // DashboardService에서 사용하는 메서드들
    @Query("SELECT c.status, COUNT(c) FROM Campaign c GROUP BY c.status")
    List<Object[]> countCampaignsByStatus();

    List<Campaign> findTop5ByOrderByRequestDateDesc();
}

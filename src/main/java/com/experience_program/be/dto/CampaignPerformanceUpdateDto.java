package com.experience_program.be.dto;

import com.experience_program.be.entity.PerformanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CampaignPerformanceUpdateDto {
    private BigDecimal actualCtr;
    private BigDecimal conversionRate;
    private PerformanceStatus performanceStatus;
    private String performanceNotes;
}

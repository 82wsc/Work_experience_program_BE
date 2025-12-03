package com.experience_program.be.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationReportDto {
    @JsonProperty("target_group_index")
    private int targetGroupIndex;

    @JsonProperty("message_draft_index")
    private int messageDraftIndex;

    @JsonProperty("spam_risk_score")
    private int spamRiskScore;

    @JsonProperty("policy_compliance")
    private String policyCompliance;

    @JsonProperty("review_summary")
    private String reviewSummary;

    @JsonProperty("recommended_action")
    private String recommendedAction;
}

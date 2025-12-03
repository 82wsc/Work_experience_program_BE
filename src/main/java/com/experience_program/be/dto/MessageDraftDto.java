package com.experience_program.be.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDraftDto {
    @JsonProperty("message_draft_index")
    private int messageDraftIndex;

    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("validation_report")
    private ValidationReportDto validationReport;
}

package com.goev.auth.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunicationChannelConfigDto {
    private String templateId;
    private Map<String, String> fields;
}

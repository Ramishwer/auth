package com.goev.auth.dto.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeTokenRequestDto {
    private String clientId;
    private String clientSecret;
    private String accessToken;
}

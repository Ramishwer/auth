package com.goev.auth.dto.session;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ExchangeTokenRequestDto {
    private String clientId;
    private String clientSecret;
    private String accessToken;
}

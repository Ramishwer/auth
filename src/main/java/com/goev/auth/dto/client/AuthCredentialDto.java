package com.goev.auth.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthCredentialDto {
    private String authKey;
    private String authSecret;
    private AuthCredentialTypeDto authCredentialType;
    private String authUUID;
}

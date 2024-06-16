package com.goev.auth.dto.client;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AuthCredentialDto {
    private String authKey;
    private String authSecret;
    private AuthCredentialTypeDto authCredentialType;
    private String authUUID;
}

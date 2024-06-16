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
public class AuthClientDto {
    private String clientKey;
    private String uuid;
    private Boolean isUserRegistrationAllowed;
    private String organizationUUID;
}

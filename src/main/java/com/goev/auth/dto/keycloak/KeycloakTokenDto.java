package com.goev.auth.dto.keycloak;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeycloakTokenDto {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("expires_in")
    private Integer expiresIn;
    @SerializedName("refresh_expires_in")
    private Integer refreshExpiresIn;
    private String scope;
    @SerializedName("session_state")
    private String sessionState;
    @SerializedName("id_token")
    private String idToken;
}

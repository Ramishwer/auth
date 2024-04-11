package com.goev.auth.dto.keycloak;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class KeycloakUserDetailsDto {
    private String sub;
    @SerializedName("email_verified")
    private Boolean emailVerified;
    private String name;
    @SerializedName("preferred_username")
    private String preferredUsername;
    @SerializedName("given_name")
    private String givenName;
    @SerializedName("family_name")
    private String familyName;
    private String email;
}
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
public class SessionDto {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private Integer refreshExpiresIn;
    private String uuid;
    private String authUUID;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String state;
    private String organizationUUID;
}

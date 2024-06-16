package com.goev.auth.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthUserDto {
    private String phoneNumber;
    private String email;
    private String uuid;
    private String organizationUUID;
    private String clientUUID;
}

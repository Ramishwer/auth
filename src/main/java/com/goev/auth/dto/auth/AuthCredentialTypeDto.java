package com.goev.auth.dto.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AuthCredentialTypeDto {
    private String name;
    private String uuid;
}

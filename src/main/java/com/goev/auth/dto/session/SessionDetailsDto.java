package com.goev.auth.dto.session;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SessionDetailsDto {
    private SessionDto details;
    private String uuid;
}

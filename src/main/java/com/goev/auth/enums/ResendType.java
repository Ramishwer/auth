package com.goev.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResendType {
    VOICE("Voice"),TEXT("text");
    private final String name;

}

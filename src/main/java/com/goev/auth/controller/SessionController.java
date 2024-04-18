package com.goev.auth.controller;


import com.goev.auth.dto.auth.AuthCredentialDto;
import com.goev.auth.dto.keycloak.KeycloakTokenDto;
import com.goev.auth.dto.session.ExchangeTokenRequestDto;
import com.goev.auth.dto.session.SessionDetailsDto;
import com.goev.auth.dto.session.SessionDto;
import com.goev.auth.service.session.SessionService;
import com.goev.lib.dto.ResponseDto;
import com.goev.lib.dto.StatusDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/session-management")
@AllArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/sessions/credential-types/{credential-type-uuid}")
    public ResponseDto<AuthCredentialDto> getSessions(@RequestParam(value = "phoneNumber")String phoneNumber,@PathVariable(value = "credential-type-uuid") String credentialType, HttpServletRequest request){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, sessionService.getSessionForSessionType(phoneNumber,credentialType));
    }
    @PostMapping("/sessions")
    public ResponseDto<SessionDto> createSession(@RequestBody AuthCredentialDto credentials){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, sessionService.createSession(credentials));
    }

    @PostMapping("/sessions/tokens")
    public ResponseDto<SessionDto> createSession(@RequestBody ExchangeTokenRequestDto token){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, sessionService.createSession(token));
    }

    @GetMapping("/sessions/{session-uuid}/token")
    public ResponseDto<SessionDto> refreshSession(@PathVariable(value = "session-uuid")String sessionUUID){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, sessionService.refreshSessionForSessionUUID(sessionUUID));
    }

    @GetMapping("/sessions/{session-uuid}")
    public ResponseDto<SessionDetailsDto> getSession(@PathVariable(value = "session-uuid")String sessionUUID){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, sessionService.getSessionDetails(sessionUUID));
    }

    @DeleteMapping("/sessions/{session-uuid}")
    public ResponseDto<Boolean> deleteSession(@PathVariable(value = "session-uuid")String sessionUUID){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, sessionService.deleteSession(sessionUUID));
    }
}

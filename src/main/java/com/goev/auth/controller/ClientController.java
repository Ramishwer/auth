package com.goev.auth.controller;


import com.goev.auth.dto.auth.AuthClientDto;
import com.goev.auth.dto.auth.AuthCredentialDto;
import com.goev.auth.dto.session.ExchangeTokenRequestDto;
import com.goev.auth.dto.session.SessionDetailsDto;
import com.goev.auth.dto.session.SessionDto;
import com.goev.auth.service.auth.AuthClientService;
import com.goev.auth.service.session.SessionService;
import com.goev.lib.dto.ResponseDto;
import com.goev.lib.dto.StatusDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/client-management")
@AllArgsConstructor
public class ClientController {

    private final AuthClientService authClientService;

    @GetMapping("/clients")
    public ResponseDto<AuthClientDto> getClientDetails(){
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(),200, authClientService.getClientDetails());
    }
}

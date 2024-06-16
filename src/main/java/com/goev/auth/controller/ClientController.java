package com.goev.auth.controller;


import com.goev.auth.dto.client.AuthClientDto;
import com.goev.auth.service.auth.AuthClientService;
import com.goev.lib.dto.ResponseDto;
import com.goev.lib.dto.StatusDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/client-management")
@AllArgsConstructor
public class ClientController {

    private final AuthClientService authClientService;

    @GetMapping("/clients")
    public ResponseDto<AuthClientDto> getClientDetails() {
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(), 200, authClientService.getClientDetails());
    }
}

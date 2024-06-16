package com.goev.auth.controller;


import com.goev.auth.dto.user.AuthUserDto;
import com.goev.auth.service.auth.AuthUserService;
import com.goev.lib.dto.ResponseDto;
import com.goev.lib.dto.StatusDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/user-management")
@AllArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/users")
    public ResponseDto<AuthUserDto> saveUser(@RequestBody AuthUserDto userDto) {
        return new ResponseDto<>(StatusDto.builder().message("SUCCESS").build(), 200, authUserService.saveUser(userDto));
    }
}

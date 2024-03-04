package com.goev.auth.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StatusController {

    @GetMapping("/api/v1/status")
    public String getStatus(){
        return "I am Alive";
    }
}

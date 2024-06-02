package com.goev.auth.event.events;

import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.event.targets.AuthTarget;
import com.goev.lib.event.core.Event;
import com.goev.lib.event.core.EventHandler;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@AllArgsConstructor
public class UserOnboardEvent extends Event<AuthUserDao> {

    @Override
    @PostConstruct
    public void init() {
        registerEventTargets(AuthTarget.getTargetName());
    }

    @Override
    public String getName() {
        return "UserOnboardEvent";
    }
}

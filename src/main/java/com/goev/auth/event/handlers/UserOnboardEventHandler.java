package com.goev.auth.event.handlers;

import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.event.targets.AuthTarget;
import com.goev.lib.event.core.Event;
import com.goev.lib.event.core.EventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class UserOnboardEventHandler extends EventHandler<AuthUserDao> {

    @Override
    public boolean onEvent(Event<AuthUserDao> event){
        return false;
    }

}

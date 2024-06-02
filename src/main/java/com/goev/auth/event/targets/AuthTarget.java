package com.goev.auth.event.targets;

import com.goev.lib.event.core.EventChannel;
import com.goev.lib.event.core.EventTarget;


public class AuthTarget extends EventTarget {
    private AuthTarget() {
    }

    public static String getTargetName() {
        return "AUTH";
    }

    public static AuthTarget getTarget(EventChannel eventChannel) {
        AuthTarget authTarget = new AuthTarget();
        authTarget.setChannel(eventChannel);
        authTarget.setName("AUTH");
        return authTarget;
    }
}

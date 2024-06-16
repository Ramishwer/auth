package com.goev.auth.event.targets;

import com.goev.auth.constant.ApplicationConstants;
import com.goev.lib.event.core.EventChannel;
import com.goev.lib.event.core.EventChannelConfiguration;
import com.goev.lib.event.core.EventTarget;


public class CentralTarget extends EventTarget {
    private CentralTarget() {
    }

    public static String getTargetName() {
        return "CENTRAL";
    }

    public static CentralTarget getTarget(EventChannel eventChannel) {
        CentralTarget centralTarget = new CentralTarget();
        centralTarget.setChannel(eventChannel);
        centralTarget.setName(getTargetName());
        centralTarget.setConfig(EventChannelConfiguration.builder()
                .hostName(ApplicationConstants.CENTRAL_URL)
                .base("/api/v1/internal")
                .path("/events")
                .build());
        return centralTarget;
    }
}

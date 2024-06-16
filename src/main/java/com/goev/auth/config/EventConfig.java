package com.goev.auth.config;

import com.goev.lib.event.core.EventChannel;
import com.goev.lib.event.core.impl.APIEventChannel;
import com.goev.lib.event.service.EventProcessor;
import com.goev.lib.event.service.impl.SimpleEventProcessor;
import com.goev.lib.services.RestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
public class EventConfig {

    @Bean
    public EventProcessor getEventProcessor(EventChannel eventChannel) {
        SimpleEventProcessor eventProcessor = new SimpleEventProcessor();
        return eventProcessor;
    }


    @Bean
    public EventChannel getEventChannel(RestClient restClient) {
        APIEventChannel eventChannel = new APIEventChannel();
        eventChannel.init(restClient);
        return eventChannel;
    }
}

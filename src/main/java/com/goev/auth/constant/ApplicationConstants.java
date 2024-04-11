package com.goev.auth.constant;

import com.goev.auth.utilities.ConstantUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ApplicationConstants {
    public static String MSG91_OTP_SMS_TEMPLATE_ID;
    public static String MSG91_MESSAGE_API_KEY;
    public static String APPLICATION_ID;

    private final ConstantUtils constantUtils;

    @PostConstruct
    void init() throws IllegalAccessException {
        constantUtils.configurationOfConstantsFromDataBase(this);
    }
}

package com.goev.auth.constant;

import com.goev.auth.utilities.ConstantUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ApplicationConstants {
    public static Boolean IS_MESSAGE_ENABLED = false;
    public static Boolean IS_WHITE_LISTING_ENABLED = false;
    public static List<String> WHITE_LIST_NUMBERS = new ArrayList<>();
    public static String MSG91_MESSAGE_API_KEY;
    public static String APPLICATION_ID;
    public static String CENTRAL_URL;
    public static String PARTNER_URL;

    public static String FIXED_PHONE_SECRET;
    public static String FIXED_EMAIL_SECRET;

    public static Boolean SHOW_SECRETS;

    public static List<Integer> PHONE_NUMBER_CREDENTIAL_TYPES = new ArrayList<>();
    public static List<Integer> EMAIL_CREDENTIAL_TYPES = new ArrayList<>();


    private final ConstantUtils constantUtils;

    @PostConstruct
    void init() throws IllegalAccessException {
        constantUtils.configurationOfConstantsFromDataBase(this);
    }
}

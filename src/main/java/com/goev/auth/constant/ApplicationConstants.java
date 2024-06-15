package com.goev.auth.constant;

import com.goev.auth.utilities.ConstantUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ConstantUtils constantUtils;

    @PostConstruct
    void init() throws IllegalAccessException {
        constantUtils.configurationOfConstantsFromDataBase(this);
    }
}

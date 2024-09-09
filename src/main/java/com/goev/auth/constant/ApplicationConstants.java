package com.goev.auth.constant;

import com.goev.auth.utilities.ConstantUtils;
import com.goev.lib.utilities.GsonDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ApplicationConstants {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(DateTime.class, new GsonDateTimeSerializer()).create();

    public static Boolean IS_MESSAGE_ENABLED = false;
    public static Boolean IS_WHITE_LISTING_ENABLED = false;
    public static List<String> WHITE_LIST_NUMBERS = new ArrayList<>();
    public static String MSG91_MESSAGE_API_KEY;
    public static String APPLICATION_ID;
    public static String FIXED_SECRET;
    public static String ADMIN_USER;
    public static Boolean SHOW_SECRETS;


    private final ConstantUtils constantUtils;

    @PostConstruct
    void init() throws IllegalAccessException {
        constantUtils.configurationOfConstantsFromDataBase(this);
    }
}

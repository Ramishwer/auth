package com.goev.auth.utilities;

import com.goev.auth.constant.ApplicationConstants;

import java.util.Random;
import java.util.UUID;

public class SecretGenerationUtils {
    private SecretGenerationUtils() {
    }

    public static String getPhoneSecret() {
        if (ApplicationConstants.FIXED_PHONE_SECRET != null)
            return ApplicationConstants.FIXED_PHONE_SECRET;
        else {
            Random generator = new Random();
            int num = generator.nextInt(899999) + 100000;
            return String.valueOf(num);

        }
    }

    public static String getEmailSecret() {
        if (ApplicationConstants.FIXED_EMAIL_SECRET != null)
            return ApplicationConstants.FIXED_EMAIL_SECRET;
        else {
            return UUID.randomUUID().toString().substring(0, 10);

        }
    }
}

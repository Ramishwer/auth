package com.goev.auth.utilities;

import com.goev.auth.constant.ApplicationConstants;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class SecretGenerationUtils {
    private static final Random GENERATOR = new Random();
    private SecretGenerationUtils() {
    }

    public static String getSecret(Integer number) {

            long num = GENERATOR.nextLong(BigDecimal.valueOf(Math.pow(10.0,number)-Math.pow(10.0,number-1.0)-1.0).longValue()) + BigDecimal.valueOf(Math.pow(10.0,number-1.0)-1).longValue();
            return String.valueOf(num);
    }
}

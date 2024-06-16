package com.goev.auth.config;

import com.goev.auth.dto.SystemCredentialDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.security.InvalidParameterException;
import java.util.Base64;

@Configuration
@Slf4j
public class SecretsManager {
    @Bean
    public SystemCredentialDto getSecret() {
        if ("local".equals(System.getenv("ENV")) || System.getenv("ENV") == null) {
            return SystemCredentialDto
                    .builder()
                    .mysqlPoolSize("5")
                    .mysqlDatabase("goevAuthLocal")
                    .mysqlHostName("localhost")
                    .mysqlUserName("root")
                    .mysqlPassword("root")
                    .mysqlPort("3307")
                    .build();
        }
        log.info("Env : {}", System.getenv("ENV"));
        String secretName = System.getenv("ENV") + "/auth/mysql";
        Region region = Region.of("ap-south-1");
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();
        String secret = null, decodedBinarySecret = null;
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse = null;
        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException | InternalServiceErrorException |
                 InvalidParameterException | InvalidRequestException | ResourceNotFoundException e) {
            throw e;
        }
        if (getSecretValueResponse.secretString() != null) {
            secret = getSecretValueResponse.secretString();
        } else {
            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
        }
        return new Gson().fromJson(secret, SystemCredentialDto.class);
    }
}


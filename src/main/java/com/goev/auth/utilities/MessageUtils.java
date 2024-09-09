package com.goev.auth.utilities;

import com.goev.auth.constant.ApplicationConstants;
import com.goev.auth.dao.client.AuthClientDao;
import com.goev.auth.dao.client.AuthCredentialTypeDao;
import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.dto.MessageResponseDto;
import com.goev.auth.dto.client.CommunicationChannelConfigDto;
import com.goev.auth.enums.CommunicationChannelType;
import com.goev.lib.services.RestClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
@AllArgsConstructor
public class MessageUtils {
    private final RestClient restClient;

    public boolean sendMessage(AuthClientDao clientDao, AuthCredentialTypeDao credentialTypeDao, AuthUserDao authUserDao, String secret) {
        if (!Boolean.TRUE.equals(ApplicationConstants.IS_MESSAGE_ENABLED)) {
            log.info("Messages Disabled");
            return false;
        }
        switch (CommunicationChannelType.valueOf(credentialTypeDao.getCommunicationChannelType())) {
            case SMS -> {
                return sendSms(authUserDao.getPhoneNumber(), secret, clientDao);
            }
            case EMAIL -> {
                return false;
            }
        }

        return false;

    }


    private boolean sendSms(String mobileNumber, String secret, AuthClientDao clientDao) {
        if (Boolean.TRUE.equals(ApplicationConstants.IS_WHITE_LISTING_ENABLED) && !ApplicationConstants.WHITE_LIST_NUMBERS.contains(mobileNumber)) {
            log.info("Mobile number whitelisting enabled and mobile number not present in list");
            return false;
        }
        try {
            List<MediaType> accept = new ArrayList<>();
            accept.add(MediaType.APPLICATION_JSON);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("authkey", ApplicationConstants.MSG91_MESSAGE_API_KEY);
            headers.setAccept(accept);

            CommunicationChannelConfigDto configDto = ApplicationConstants.GSON.fromJson(clientDao.getCommunicationChannelConfig(), CommunicationChannelConfigDto.class);
            Map<String, String> params = new TreeMap<>(configDto.getFields());
            params.put("OTP", secret);
            String response = restClient.post("https://control.msg91.com/api/v5/otp?mobile=91" + mobileNumber + "&template_id=" + configDto.getTemplateId(), headers, params, String.class, true, false);
            MessageResponseDto responseDto = new Gson().fromJson(response, new TypeToken<MessageResponseDto>() {
            }.getType());
            return responseDto.getType().equals("success");
        } catch (Exception e) {
            log.info("Error in sending message to " + mobileNumber + "error " + e);
            return false;
        }
    }
}

package com.goev.auth.utilities;

import com.goev.auth.constant.ApplicationConstants;
import com.goev.auth.dto.MessageResponseDto;
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

    public boolean sendMessage(String mobileNumber, String otp) {
        try {
            List<MediaType> accept = new ArrayList<>();
            accept.add(MediaType.APPLICATION_JSON);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("authkey", ApplicationConstants.MSG91_MESSAGE_API_KEY);
            headers.setAccept(accept);
            Map<String, String> params = new TreeMap<>();
            params.put("OTP", otp);
            String response = restClient.post("https://control.msg91.com/api/v5/otp?mobile=91" + mobileNumber + "&template_id=" + ApplicationConstants.MSG91_OTP_SMS_TEMPLATE_ID, headers, params, String.class, true, false);
            MessageResponseDto responseDto = new Gson().fromJson(response, new TypeToken<MessageResponseDto>() {
            }.getType());
            return responseDto.getType().equals("success");
        } catch (Exception e) {
            log.info("Error in sending message to " + mobileNumber + "error " + e);
            return false;
        }
    }
}
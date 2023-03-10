package org.softuni.eventures.service.impl;

import org.softuni.eventures.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {
    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecretKey;

    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public RecaptchaServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public String verifyRecaptcha(String userIpAddress, String gRecaptchaResponse) {
        Map<String, Object> recaptchaVerifyRequestBody = new HashMap<>();

        recaptchaVerifyRequestBody.put("secret", recaptchaSecretKey);
        recaptchaVerifyRequestBody.put("response", gRecaptchaResponse);
        recaptchaVerifyRequestBody.put("remoteip", userIpAddress);


        ResponseEntity<Map> recaptchaVerifyResponse =
                this.restTemplateBuilder
                        .build()
                        .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL +
                                "?secret={secret}&response={response}&remoteip={remoteip}",
                                recaptchaVerifyRequestBody, Map.class, recaptchaVerifyRequestBody);

        Map<String, Object> recaptchaVerifyResponseBody =
                recaptchaVerifyResponse.getBody();

        if ((Boolean) recaptchaVerifyResponseBody.get("success")) {
            return "success";
        } else {
            return null;
        }
    }
}

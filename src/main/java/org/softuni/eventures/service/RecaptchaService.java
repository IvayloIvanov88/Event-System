package org.softuni.eventures.service;

public interface RecaptchaService {
    String verifyRecaptcha(String userIpAddress, String gRecaptchaResponse);
}

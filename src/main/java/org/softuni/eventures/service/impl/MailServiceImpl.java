package org.softuni.eventures.service.impl;

import org.softuni.eventures.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private static final String REGISTRATION_MAIL_SUBJECT =
            "Eventures Inc. - [Registration]";

    private static final String REGISTRATION_SUCCESS_MESSAGE
            = "Welcome, %s! You have successfully registered your user to Eventures Ticket Service.";

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sentRegistrationSuccessMessage(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(REGISTRATION_MAIL_SUBJECT);
        message.setText(String.format(REGISTRATION_SUCCESS_MESSAGE, username));

        message.setFrom("info@eventures.io");
        message.setTo(email);

        this.javaMailSender.send(message);
    }
}

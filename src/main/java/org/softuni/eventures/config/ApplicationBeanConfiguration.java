package org.softuni.eventures.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class ApplicationBeanConfiguration {
    @Value("${spring.activemq.broker-url}")
    private String DEFAULT_BROKER_URL;

    @Value("${spring.mail.properties.port}")
    private int MAIL_PORT;

    @Value("${spring.mail.properties.host}")
    private String MAIL_HOST;

    @Value("${spring.mail.properties.username}")
    private String MAIL_USERNAME;

    @Value("${spring.mail.properties.password}")
    private String MAIL_PASSWORD;



    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory
                = new ActiveMQConnectionFactory();

        activeMQConnectionFactory.setBrokerURL(DEFAULT_BROKER_URL);

        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());

        return jmsTemplate;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(MAIL_HOST);
        javaMailSender.setUsername(MAIL_USERNAME);
        javaMailSender.setPassword(MAIL_PASSWORD);
        javaMailSender.setPort(MAIL_PORT);

        return javaMailSender;
    }
}

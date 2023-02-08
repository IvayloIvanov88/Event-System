package org.softuni.eventures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class EventuresApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventuresApplication.class, args);
    }
}

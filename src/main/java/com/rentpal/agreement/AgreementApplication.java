package com.rentpal.agreement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.rentpal.agreement.filters"})

public class AgreementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgreementApplication.class, args);
    }

}

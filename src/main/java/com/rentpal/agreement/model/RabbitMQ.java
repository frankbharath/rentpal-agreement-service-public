package com.rentpal.agreement.model;

/*
 * @author frank
 * @created 23 Dec,2020 - 12:38 AM
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "app.settings.rabbitmq")
public class RabbitMQ {
    String queue;
    String exchange;
    String routingKey;
}

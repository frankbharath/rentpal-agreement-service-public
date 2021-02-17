package com.rentpal.agreement.configuration;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.model.Mail;
import com.rentpal.agreement.model.OAuthDetails;
import com.rentpal.agreement.model.RabbitMQ;
import com.rentpal.agreement.service.interfaces.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.mail.MessagingException;
import java.util.Properties;

/*
 * @author frank
 * @created 23 Dec,2020 - 6:53 PM
 */

/**
 * The type Spring config.
 */
@Configuration
@ServletComponentScan(basePackages = {"com.rentpal.agreement.filters"})
@EnableConfigurationProperties
@Slf4j
public class SpringConfig {

    private final RabbitMQ rabbitMQ;

    private EmailService emailService;

    /** The Environment variable to read application properties.*/
    @Autowired
    private Environment env;

    /**
     * Instantiates a new Spring config.
     *
     * @param rabbitMQ the rabbit mq
     */
    @Autowired
    public SpringConfig(RabbitMQ rabbitMQ){
        this.rabbitMQ=rabbitMQ;
    }

    /**
     * Sets email service.
     *
     * @param emailService the email service
     */
    @Autowired
    public void setEmailService(EmailService emailService){
        this.emailService=emailService;
    }

    /**
     * Gets the oauth details.
     *
     * @return the oauth details
     */
    @Bean
    @ConfigurationProperties("spring.security.oauth2")
    public OAuthDetails getOAuthDetails() {
        return new OAuthDetails();
    }

    /**
     * Queue for sending notifications.
     *
     * @return the queue
     */
    @Bean
    public Queue queue(){
        return QueueBuilder.durable(rabbitMQ.getQueue()).deadLetterExchange(Constants.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(Constants.DEAD_LETTER_ROUTING_KEY).build();
    }

    /**
     * Dead letter queue when to store failed messages.
     *
     * @return the queue
     */
    @Bean
    @Qualifier("deadLetterQueue")
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(Constants.DEAD_LETTER_QUEUE_NAME).build();
    }

    /**
     * Topic exchange for notification.
     *
     * @return the topic exchange
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(rabbitMQ.getExchange());
    }

    /**
     * Dead letter exchange to forward failed messages to dead letter queue.
     *
     * @return the topic exchange
     */
    @Bean
    @Qualifier("deadLetterExchange")
    public TopicExchange deadLetterExchange(){
        return new TopicExchange(Constants.DEAD_LETTER_EXCHANGE);
    }

    /**
     * Binds notification queue with exchange for a given routing key.
     *
     * @param queue         the queue
     * @param topicExchange the topic exchange
     * @return the binding
     */
    @Bean
    public Binding bindingBuilder(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(rabbitMQ.getRoutingKey());
    }

    /**
     * Binds dead letter queue with exchange for a given routing key.
     *
     * @param queue         the queue
     * @param topicExchange the topic exchange
     * @return the binding
     */
    @Bean
    public Binding bindingBuilderDLX(@Qualifier("deadLetterQueue") Queue queue, @Qualifier("deadLetterExchange") TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(Constants.DEAD_LETTER_ROUTING_KEY);
    }

    /**
     * Converts bytes of stream to JSON
     *
     * @return the message converter
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitMQ template to send messages to consumer.
     *
     * @param connectionFactory the connection factory
     * @return the amqp template
     */
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    /**
     * Consumes message from notification queue.
     *
     * @param mail the mail
     * @throws MessagingException the messaging exception
     */
    @RabbitListener(queues = "${app.settings.rabbitmq.queue}")
    public void sendEmailNotification(Mail mail) throws MessagingException {
        emailService.sendEmail(mail);
    }

    /**
     * Mail sender java mail sender.
     *
     * @return the java mail sender
     */
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender= new JavaMailSenderImpl();
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
        Properties properties=new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST","PUT", "DELETE")
                        .exposedHeaders("X-Total-Count");
            }
        };
    }
}

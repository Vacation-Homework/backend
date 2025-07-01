package com.vacation.homework.app.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter()); //배포환경에서도 JSON 처리 가능하도록
        return template;
    }

    // === 기존 일기용 ===
    public static final String HOMEWORK_EXCHANGE = "homework.exchange";
    public static final String HOMEWORK_ROUTING_KEY = "homework.created";
    public static final String HOMEWORK_QUEUE = "homework.queue";

    // === 신규 알림용 ===
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.key";
    public static final String NOTIFICATION_QUEUE = "notification.queue";


    // === 기존 일기용 ===
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(HOMEWORK_EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(HOMEWORK_QUEUE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(HOMEWORK_ROUTING_KEY);
    }

    // === 신규 알림용 ===
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }
}
package com.techie.microservices.notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Entry point for the Notification Service.
 * <p>
 * Configures RabbitMQ infrastructure (queue, exchange, binding) so that the
 * necessary messaging artifacts are created automatically at startup.
 */
@SpringBootApplication(scanBasePackages = "com.techie.microservices.notification")
@EnableRabbit
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    // -----------------------------------------------------------------------
    // RabbitMQ configuration
    // -----------------------------------------------------------------------

    /**
     * The queue that receives notification messages.
     */
    @Bean
    public Queue notificationQueue() {
        // durable queue; will be created if it does not exist
        return new Queue("notification.queue", true);
    }

    /**
     * Direct exchange used for routing notification messages.
     */
    @Bean
    public DirectExchange notificationExchange() {
        // durable exchange; will be created automatically
        return new DirectExchange("notification.exchange", true, false);
    }

    /**
     * Binds the {@code notificationQueue} to the {@code notificationExchange}
     * with a routing key.
     */
    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with("notification.routingkey");
    }

    /**
     * RabbitTemplate for publishing messages.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
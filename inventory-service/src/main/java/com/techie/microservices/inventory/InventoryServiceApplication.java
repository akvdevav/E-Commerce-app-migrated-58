package com.techie.microservices.inventory;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "com.techie.microservices.inventory")
@EnableRabbit
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Configuration
    static class RabbitConfig {

        // Queue for inventory events
        @Bean
        public Queue inventoryQueue() {
            return new Queue("inventory.queue", true);
        }

        // Direct exchange for inventory routing
        @Bean
        public DirectExchange inventoryExchange() {
            return new DirectExchange("inventory.exchange");
        }

        // Binding between queue and exchange with a routing key
        @Bean
        public Binding inventoryBinding(Queue inventoryQueue, DirectExchange inventoryExchange) {
            return BindingBuilder.bind(inventoryQueue)
                    .to(inventoryExchange)
                    .with("inventory.routingkey");
        }
    }

    @Configuration
    static class CacheConfig {

        // Configure Redis (Valkey) based CacheManager
        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                            new GenericJackson2JsonRedisSerializer()));
            return RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(config)
                    .build();
        }
    }
}
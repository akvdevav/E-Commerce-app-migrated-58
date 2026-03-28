package com.programming.techie.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	/**
	 * Default DataSource configuration.
	 * Uses PostgreSQL with fallback values that match the required credentials.
	 * The URL can be overridden via {@code spring.datasource.url} property.
	 */
	@Configuration
	static class DataSourceConfig {

		@Bean
		public DataSource dataSource(
				@Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/postgres}") String url,
				@Value("${spring.datasource.username:postgres}") String username,
				@Value("${spring.datasource.password:postgres}") String password) {
			return DataSourceBuilder.create()
					.driverClassName("org.postgresql.Driver")
					.url(url)
					.username(username)
					.password(password)
					.build();
		}
	}
}
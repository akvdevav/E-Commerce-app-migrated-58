package com.techie.microservices.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import jakarta.sql.DataSource;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCaching
@EnableJpaRepositories(basePackages = "com.techie.microservices.inventory")
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	/**
	 * Configures a PostgreSQL {@link DataSource}. Credentials are set to the default
	 * values required by the CI environment (postgres/postgres). Adjust the URL,
	 * username, or password as needed for local development.
	 */
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.driverClassName("org.postgresql.Driver")
				.url("jdbc:postgresql://localhost:5432/inventory")
				.username("postgres")
				.password("postgres")
				.build();
	}
}
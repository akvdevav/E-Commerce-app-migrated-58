package com.techie.microservices.product.repository;

import com.techie.microservices.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
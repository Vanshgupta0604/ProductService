package com.productservice.repositories;

import com.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductServiceRepository extends JpaRepository<Product, Long> {

}
package com.productservice.service;

import com.productservice.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProduct();

    Product getSingleProduct(Long productId);

    Product addProduct(Product product);

    Product updateProduct(Long productId, Product product);

    Product replaceProduct(Long productId, Product product);

    boolean deleteProduct(Long productId);
}

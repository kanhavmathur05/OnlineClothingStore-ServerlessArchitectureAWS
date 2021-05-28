package com.productservice.repository;

import java.util.List;

import com.productservice.model.Product;

public interface ProductRepository {

	Product addProduct(Product product);

	List<Product> getAllProducts();

	void deleteProduct(String productId);

	Product getProductById(String productId);
}

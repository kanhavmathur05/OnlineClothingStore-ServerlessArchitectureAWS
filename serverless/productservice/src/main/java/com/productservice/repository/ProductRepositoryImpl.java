package com.productservice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.productservice.model.Product;

@Component
public class ProductRepositoryImpl implements ProductRepository {

	@Autowired
	DynamoDBMapper mapper;

	@Override
	public Product addProduct(Product product) {
		mapper.save(product);
		return product;
	}

	@Override
	public List<Product> getAllProducts() {
		return mapper.scan(Product.class, new DynamoDBScanExpression());
	}

	@Override
	public void deleteProduct(String productId) {
		mapper.delete(mapper.load(Product.class, productId));
	}

	@Override
	public Product getProductById(String productId) {
		return mapper.load(Product.class, productId);
	}

}

package com.orderandcartservice.repository;

import java.util.List;

import com.orderandcartservice.model.CartProduct;

public interface CartProductsRepository {

	CartProduct addProductToCart(CartProduct cartProduct);
	
	void removeProductFromCart(String cartProductId);
	
	List<CartProduct> getProductsInCartForUser(String username);
	
	void placeOrder(String username);
}

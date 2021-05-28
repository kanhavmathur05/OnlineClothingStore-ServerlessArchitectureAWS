package com.orderandcartservice.repository;

import java.util.List;

import com.orderandcartservice.model.OrderHistory;

public interface OrderHistoryRepository {

//	List<OrderHistory> addCartProductsToOrderHistory(List<OrderHistory> cartProducts);
	
	List<OrderHistory> getOrderHistoryForUser(String username);
	
	void clearOrderHistory(String username);
	
	
}

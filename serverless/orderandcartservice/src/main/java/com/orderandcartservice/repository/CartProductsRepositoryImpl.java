package com.orderandcartservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.orderandcartservice.model.CartProduct;
import com.orderandcartservice.model.OrderHistory;

@Component
public class CartProductsRepositoryImpl implements CartProductsRepository {

	@Autowired
	DynamoDBMapper mapper;

	@Override
	public CartProduct addProductToCart(CartProduct cartProduct) {
		mapper.save(cartProduct);
		return cartProduct;
	}

	@Override
	public void removeProductFromCart(String cartProductId) {
		mapper.delete(mapper.load(CartProduct.class, cartProductId));
	}

	@Override
	public List<CartProduct> getProductsInCartForUser(String username) {
		Condition condition = new Condition();
		condition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(username));

		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("username", condition);
		return mapper.scan(CartProduct.class, scanExpr);
	}

	@Override
	public void placeOrder(String username) {
		Condition condition = new Condition();
		condition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(username));

		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("username", condition);
		List<CartProduct> cartProducts = mapper.scan(CartProduct.class, scanExpr);

		List<OrderHistory> orderHistoryList = new ArrayList<OrderHistory>();
		for (CartProduct cartItem : cartProducts) {
			OrderHistory orderHistory = new OrderHistory();
			orderHistory.setPrice(cartItem.getPrice());
			orderHistory.setProductDescription(cartItem.getProductDescription());
			orderHistory.setProductId(cartItem.getProductId());
			orderHistory.setProductImage(cartItem.getProductImage());
			orderHistory.setProductName(cartItem.getProductName());
			orderHistory.setUsername(username);
			orderHistoryList.add(orderHistory);

//			mapper.delete(mapper.load(CartProduct.class, cartItem.getCartProductId()));
		}
		mapper.batchSave(orderHistoryList);
		mapper.batchDelete(cartProducts);
	}

}

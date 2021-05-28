package com.orderandcartservice.repository;

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
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {

	@Autowired
	DynamoDBMapper mapper;

	@Override
	public void clearOrderHistory(String username) {
		Condition condition = new Condition();
		condition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(username));

		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("username", condition);
		List<OrderHistory> orderHistoryofUser = mapper.scan(OrderHistory.class, scanExpr);

		mapper.batchDelete(orderHistoryofUser);
	}

	@Override
	public List<OrderHistory> getOrderHistoryForUser(String username) {
		Condition condition = new Condition();
		condition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(username));

		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("username", condition);
		List<OrderHistory> orderHistoryofUser = mapper.scan(OrderHistory.class, scanExpr);
		return orderHistoryofUser;
	}

}

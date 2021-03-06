package com.orderandcartservice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "order_history")
public class OrderHistory {

	@DynamoDBHashKey(attributeName = "orderHistoryId")
	@DynamoDBAutoGeneratedKey
	private String orderHistoryId;

	@DynamoDBAttribute
	private String productId;

	@DynamoDBAttribute
	private String productName;

	@DynamoDBAttribute
	private String productImage;

	@DynamoDBAttribute
	private String productDescription;

	@DynamoDBAttribute
	private int price;

	@DynamoDBAttribute
	private String username;

	public String getOrderHistoryId() {
		return orderHistoryId;
	}

	public void setOrderHistoryId(String orderHistoryId) {
		this.orderHistoryId = orderHistoryId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "OrderHistory [orderHistoryId=" + orderHistoryId + ", productId=" + productId + ", productName="
				+ productName + ", productImage=" + productImage + ", productDescription=" + productDescription
				+ ", price=" + price + ", username=" + username + "]";
	}

	public OrderHistory(String orderHistoryId, String productId, String productName, String productImage,
			String productDescription, int price, String username) {
		super();
		this.orderHistoryId = orderHistoryId;
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.productDescription = productDescription;
		this.price = price;
		this.username = username;
	}

	public OrderHistory() {
		super();
	}

}

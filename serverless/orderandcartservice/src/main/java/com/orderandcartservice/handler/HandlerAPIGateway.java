package com.orderandcartservice.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderandcartservice.OrderandcartserviceApplication;
import com.orderandcartservice.model.CartProduct;
import com.orderandcartservice.repository.CartProductsRepository;
import com.orderandcartservice.repository.OrderHistoryRepository;

public class HandlerAPIGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static ApplicationContext applicationContext = SpringApplication.run(OrderandcartserviceApplication.class);

	private CartProductsRepository cartProductsRepository;

	private OrderHistoryRepository orderHistoryRepository;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		this.cartProductsRepository = applicationContext.getBean(CartProductsRepository.class);
		this.orderHistoryRepository = applicationContext.getBean(OrderHistoryRepository.class);
		ObjectMapper objectMapper = new ObjectMapper();
		String method = input.getHttpMethod();
		String resource = input.getResource();
		System.out.println("===PATH=======  " + input.getPath() + "  ===========");
		System.out.println("=====METHOD=====  " + method + "  ===========");
		System.out.println("=======RESOURCe===  " + resource + "  ===========");
		Map<String, String> queryParams = input.getQueryStringParameters();
		if (resource.equals("/cart")) {
			System.out.println("******* Add To Cart Called*****");
			if (method.equals("GET") && queryParams != null && queryParams.containsKey("username")) {

				String username = queryParams.get("username");
				String result = makeJSONStringFromObject(objectMapper,
						cartProductsRepository.getProductsInCartForUser(username));
				return buildResponse(200, result);
			} else if (method.equals("POST") && queryParams == null) {
				String body = input.getBody();
				CartProduct cartProduct = null;
				String result = "[]";
				try {
					cartProduct = objectMapper.readValue(body, CartProduct.class);
					if (cartProduct != null) {
						result = objectMapper.writeValueAsString(cartProductsRepository.addProductToCart(cartProduct));
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return buildResponse(500, "error occured!!");
				}
			} else if (method.equals("DELETE") && queryParams != null && queryParams.containsKey("cartProductId")) {
				String cartProductId = queryParams.get("cartProductId");
				cartProductsRepository.removeProductFromCart(cartProductId);
				return buildResponse(200, "Item successfully deleted from cart");
			}
		} else if (resource.equals("/placeorder") && queryParams != null && queryParams.containsKey("username")) {
			if(method.equals("GET")) {
				String username = queryParams.get("username");
				cartProductsRepository.placeOrder(username);
				return buildResponse(200, "Order Successfully Placed");				
			}
			else {
				return buildResponse(500, "Something Went Wrong");
			}
		} else if (resource.equals("/orderhistory")) {
			if (method.equals("GET") && queryParams.containsKey("username")) {
				String username = queryParams.get("username");
				String result = makeJSONStringFromObject(objectMapper,
						orderHistoryRepository.getOrderHistoryForUser(username));
				return buildResponse(200, result);
			} else if (method.equals("DELETE") && queryParams != null && queryParams.containsKey("username")) {
				String username = queryParams.get("username");
				orderHistoryRepository.clearOrderHistory(username);
				return buildResponse(200, "Order History Successfully cleared");
			}
		} else {
			System.out.println("************nothing called*************");
		}
		return buildResponse(500, "");
	}

	public static APIGatewayProxyResponseEvent buildResponse(int status, String obj) {
		APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent();
		res.setStatusCode(status);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Access-Control-Allow-Headers", "*");
		map.put("Access-Control-Allow-Origin", "*");
		map.put("Access-Control-Allow-Methods", "*");
		res.withHeaders(map);
//		res.setHeaders(map);
		res.setBody(obj);
		return res;
	}

	public static String makeJSONStringFromObject(ObjectMapper objectMapper, Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return "";
		}
	}

}
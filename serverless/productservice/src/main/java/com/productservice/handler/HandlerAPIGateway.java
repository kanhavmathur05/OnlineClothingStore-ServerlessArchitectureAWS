package com.productservice.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.ProductserviceApplication;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;

public class HandlerAPIGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static ApplicationContext applicationContext=SpringApplication.run(ProductserviceApplication.class);
	
	private ProductRepository productRepository;
	
	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		this.productRepository=applicationContext.getBean(ProductRepository.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String method = input.getHttpMethod();
		String resource = input.getResource();
		System.out.println("===PATH=======  " + input.getPath() + "  ===========");
		System.out.println("=====METHOD=====  " + method + "  ===========");
		System.out.println("=======RESOURCe===  " + resource + "  ===========");
		Map<String, String> pathParams = input.getPathParameters();
		Map<String, String> queryParams = input.getQueryStringParameters();
		System.out.println("=============Query params ID============"+queryParams);
		System.out.println("=============PATH params ID============"+pathParams);
		if(resource.equals("/product") && queryParams==null)
		{
			if(method.equals("POST")) {
				System.out.println("============Add Product Called============");
				String body=input.getBody();
				Product product=null;
				String result="[]";
				
				try {
					product=objectMapper.readValue(body, Product.class);
					if(product!=null) {
						result = objectMapper.writeValueAsString(productRepository.addProduct(product));
						return buildResponse(200, result);
					}
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
					return buildResponse(500, "error occured!!");
				}				
			}
			
			else if(method.equals("GET")) {
				//Get All Products
				String result=makeJSONStringFromObject(objectMapper, productRepository.getAllProducts());
				return buildResponse(200, result);
			}

		} else if(resource.equals("/product") && queryParams!=null)
		{
			System.out.println("In get method of product");
			if(method.equals("GET") && queryParams.containsKey("productId")){
				System.out.println("=========In get with Params method=========");
				String productId=queryParams.get("productId");
				System.out.println("Value of Product Id: "+productId);
				String result=makeJSONStringFromObject(objectMapper, productRepository.getProductById(productId));
				return buildResponse(200, result);
			}
			
			else if(method.equals("DELETE") && queryParams.containsKey("productId")) {
				String productId=queryParams.get("productId");
				productRepository.deleteProduct(productId);
				return buildResponse(200, "Product Successfullt deleted");
			}
		}
		else {
			System.out.println("============Nothing Called============");
		}
		
		return buildResponse(200, "No Method was called");
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

package com.spree;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Create_Cart_AddItem_Associate_User {
	String accessToken;
	String userId;
	String cartToken;
	JsonPath jsonPathEvaluator;
	Response response;

	@BeforeClass
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
		response = RestAssured.given().auth().oauth2(accessToken)
				.get("https://demo.spreecommerce.org/api/v2/storefront/account").then().extract().response();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		jsonPathEvaluator = response.getBody().jsonPath();
		userId = jsonPathEvaluator.get("data.id").toString();
	}

	@Test(priority = 1)
	public void createCart() {
		response = RestAssured.given().contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/cart").then().extract().response();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		jsonPathEvaluator = response.getBody().jsonPath();
		cartToken = jsonPathEvaluator.get("data.attributes.token").toString();
		System.out.println(cartToken);
	}

	@Test (priority = 2)
	public void addItemToCart() {
		JSONObject body = new JSONObject();
		body.put("variant_id", "1");
		body.put("quantity", 5);
		response = RestAssured.given()
				.header("X-Spree-Order-Token", cartToken)
				.contentType(ContentType.JSON)
				.body(body)
				.post("https://demo.spreecommerce.org/api/v2/storefront/cart/add_item")
				.then().extract().response();
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		jsonPathEvaluator = response.getBody().jsonPath();
		int numItems = Integer.parseInt(jsonPathEvaluator.get("data.attributes.item_count").toString());
		Assert.assertEquals(5, numItems);
	}
	
	@Test (priority = 3)
	public void associateCartToUser() {
		response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.param("guest_order_token", cartToken)
				.contentType(ContentType.JSON)
				.patch("https://demo.spreecommerce.org/api/v2/storefront/cart/associate")
				.then().extract().response();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		jsonPathEvaluator = response.getBody().jsonPath();
		String userIdFromCart = jsonPathEvaluator.get("data.relationships.user.data.id").toString();
		Assert.assertEquals(userIdFromCart, userId);
		
	}
	
	@Test(priority = 4)
	public void deleteCart() {
		response = RestAssured.given().header("X-Spree-Order-Token", cartToken)
				.delete("https://demo.spreecommerce.org/api/v2/storefront/cart").then().extract().response();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(204, statusCode);
	}
	
}

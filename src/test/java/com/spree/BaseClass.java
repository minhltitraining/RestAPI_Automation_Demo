package com.spree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseClass {
	
	public static String oAuth_Token() {
		String accessToken;
		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("grant_type", "password");
		requestParams.put("username", "minh@spree.com");
		requestParams.put("password", "123456");
		request.header("Content-Type", "application/json");
		request.body(requestParams.toJSONString());
		Response response = request.post("/spree_oauth/token");
		response.prettyPrint();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		String responseBody = response.getBody().asString();
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		accessToken = jsonPathEvaluator.get("access_token").toString();
		System.out.println("oAuth Token is =>  " + accessToken);
		return accessToken;
	}
	
	public static void deleteAllAddresses(String accessToken) {
		
		//get list of all address id
		List<String> addressIds = new ArrayList<String>();
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.get("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then().extract().response();
		Assert.assertEquals(200, response.getStatusCode());
		JsonPath jsonPathEva = response.getBody().jsonPath();
		ArrayList<Map<String, String>> data = jsonPathEva.get("data");
		for (Map<String, String> address: data) {
			addressIds.add(address.get("id"));
		}
		
		//delete all address
		for (String id: addressIds) {
			response = RestAssured.given()
					.auth()
					.oauth2(accessToken)
					.delete("https://demo.spreecommerce.org/api/v2/storefront/account/addresses/" + id)
					.then().extract().response();
			Assert.assertEquals(204, response.getStatusCode());
		}
		response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.get("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then().extract().response();
		 JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		 int numAddresses = Integer.parseInt(jsonPathEvaluator.get("meta.count").toString()) ;
		 Assert.assertEquals(numAddresses, 0);
	}
}

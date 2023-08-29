package com.spree;

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
}

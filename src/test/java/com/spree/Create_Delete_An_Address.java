package com.spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_Delete_An_Address {
	String addressId;
	String accessToken;

	@BeforeClass()
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
	}
	
	
	@Test(priority = 1)
	public void createAddress() throws IOException, ParseException {
		JSONParser jsonparser = new JSONParser();
		FileReader reader = new FileReader(".\\TestData\\address.json");
		Object obj = jsonparser.parse(reader);
		JSONObject prodjsonobj = (JSONObject) obj;
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.contentType(ContentType.JSON)
				.body(prodjsonobj)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then()
				.extract().response();
		response.getBody().prettyPrint();
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		addressId = jsonPathEvaluator.get("data.id").toString();
		System.out.println(" id is =>  " + addressId);
	}
	
	@Test(priority=2)
	public void deleteAddress() {
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.delete("https://demo.spreecommerce.org/api/v2/storefront/account/addresses/" + addressId)
				.then()
				.extract()
				.response();
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(204, statusCode);
	}
}

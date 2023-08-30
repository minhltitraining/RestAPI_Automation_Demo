package com.spree;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Create_Multiple_Addresses_Using_TestData {
	String accessToken;
	@BeforeClass
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
		BaseClass.deleteAllAddresses(accessToken);
	}
	
	@Test(dataProvider = "Addresses", dataProviderClass = Spreecom_TestData.class, priority = 1)
	public void addAddress(String fName, String lName, String address1,
			String city, String zipcode, String phone, String state, String country) {
		JSONObject newAddress = new JSONObject();
		newAddress.put("firstname", fName);
		newAddress.put("lastname", lName);
		newAddress.put("address1", address1);
		newAddress.put("city", city);
		newAddress.put("zipcode", zipcode);
		newAddress.put("phone", phone);
		newAddress.put("state_name", state);
		newAddress.put("country_iso", country);
		JSONObject body = new JSONObject();
		body.put("address", newAddress);
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.body(body)
				.contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then()
				.extract()
				.response();
		 int statusCode = response.getStatusCode();
		 Assert.assertEquals(200, statusCode);
	}
	
	@Test(priority = 2)
	public void countAddress() {
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.get("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then()
				.extract()
				.response();
		 int statusCode = response.getStatusCode();
		 Assert.assertEquals(200, statusCode);
		 JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		 Integer numAddresses = Integer.parseInt(jsonPathEvaluator.get("meta.count").toString()) ;
		 Assert.assertEquals(numAddresses, 5);
	}
}

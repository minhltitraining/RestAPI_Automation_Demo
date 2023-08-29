package com.spree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;

public class Delete_All_Addresses {

	String accessToken;
	List<String> addressIds;
	@BeforeClass
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
		addressIds = new ArrayList<String>();
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
	}
	
	@Test
	public void deleteAllAddresses() {
		for (String id: addressIds) {
			Response response = RestAssured.given()
					.auth()
					.oauth2(accessToken)
					.delete("https://demo.spreecommerce.org/api/v2/storefront/account/addresses/" + id)
					.then().extract().response();
			Assert.assertEquals(204, response.getStatusCode());
		}
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.get("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then().extract().response();
		 JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		 int numAddresses = Integer.parseInt(jsonPathEvaluator.get("meta.count").toString()) ;
		 Assert.assertEquals(numAddresses, 0);
		
	}
	
	
	
}

package com.spree;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Create_Bad_Addresses {
	String accessToken;

	@BeforeClass
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
		// delete all addresses
		BaseClass.deleteAllAddresses(accessToken);
	}

	@Test(dataProvider = "addressWithLabel", dataProviderClass = Spreecom_TestData.class, priority = 1)
	public void addSameAddressLable(String fName, String lName, String address1, String city, String zipcode,
			String phone, String state, String country, String label) {
		JSONObject newAddress = new JSONObject();
		newAddress.put("firstname", fName);
		newAddress.put("lastname", lName);
		newAddress.put("address1", address1);
		newAddress.put("city", city);
		newAddress.put("zipcode", zipcode);
		newAddress.put("phone", phone);
		newAddress.put("state_name", state);
		newAddress.put("country_iso", country);
		newAddress.put("label", label);
		JSONObject body = new JSONObject();
		body.put("address", newAddress);
		Response response = RestAssured.given().auth().oauth2(accessToken).body(body).contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then().extract().response();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		// check label Work exist
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String actLabel = jsonPathEvaluator.get("data.attributes.label").toString();
		Assert.assertEquals(actLabel, label);

		// Add same address with same label
		response = RestAssured.given().auth().oauth2(accessToken).body(body).contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then().extract().response();
		statusCode = response.getStatusCode();
		Assert.assertEquals(422, statusCode);
		jsonPathEvaluator = response.getBody().jsonPath();
		String generalErr = jsonPathEvaluator.get("error").toString();
		Assert.assertEquals(generalErr, "Address name has already been taken");
		String specificErr = jsonPathEvaluator.get("errors.label").toString();
		Assert.assertEquals(specificErr, "[has already been taken]");
	}

	@Test(dataProvider = "BadAddresses", dataProviderClass = Spreecom_TestData.class, priority = 2)
	public void addBadAddresses(String fName, String lName, String address1, String city, String zipcode, String phone,
			String state, String country, String expResult) {
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
		Response response = RestAssured.given().auth().oauth2(accessToken).body(body).contentType(ContentType.JSON)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then().extract().response();
		int statusCode = response.getStatusCode();
		if (expResult.equals("emptyCountry")) {
			Assert.assertEquals(500, statusCode);
		} else {
			Assert.assertEquals(422, statusCode);
		}

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		jsonPathEvaluator = response.getBody().jsonPath();
		String generalErr = jsonPathEvaluator.get("error").toString();
		String specificErr = "";
		switch (expResult) {
		case "emptyFirstname":
			Assert.assertEquals(generalErr, "First Name can't be blank");
			specificErr = jsonPathEvaluator.get("errors.firstname").toString();
			break;
		case "emptyLastname":
			Assert.assertEquals(generalErr, "Last Name can't be blank");
			specificErr = jsonPathEvaluator.get("errors.lastname").toString();
			break;
		case "emptyAddress1":
			Assert.assertEquals(generalErr, "Address can't be blank");
			specificErr = jsonPathEvaluator.get("errors.address1").toString();
			break;
		case "emptyCity":
			Assert.assertEquals(generalErr, "City can't be blank");
			specificErr = jsonPathEvaluator.get("errors.city").toString();
			break;
		case "emptyZipcode":
			Assert.assertEquals(generalErr, "Zip Code can't be blank");
			specificErr = jsonPathEvaluator.get("errors.zipcode").toString();
			break;
		case "emptyPhoneNumber":
			Assert.assertEquals(generalErr, "Phone can't be blank");
			specificErr = jsonPathEvaluator.get("errors.phone").toString();
			break;
		case "emptyState":
			Assert.assertEquals(generalErr, "State can't be blank");
			specificErr = jsonPathEvaluator.get("errors.state").toString();
			break;
		case "emptyCountry":
			Assert.assertEquals(generalErr, "Internal Server Error");
			break;
		}
		if (!expResult.equals("emptyCountry")) {
			Assert.assertEquals(specificErr, "[can't be blank]");
		}
		
	}

}

package com.spree;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.testng.annotations.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_an_Address_also_Update_An_Address_Using_File {

	// String bearerToken = "PUx5Gt4DZ3ncE7CugwyULGLTy6BMLCXf4XztxZ_LVQ8";
	String ID;
	String accessToken;

	@BeforeTest
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
	}
	
	@Test(priority = 1)
	public void Create_Address() throws IOException, ParseException {

		// Create json object of JSONParser class to parse the JSON data
		JSONParser jsonparser = new JSONParser();
		// Create object for FileReader class, which help to load and read JSON file
		FileReader reader = new FileReader(".\\TestData\\address.json");
		// Returning/assigning to Java Object
		Object obj = jsonparser.parse(reader);
		// Convert Java Object to JSON Object, JSONObject is typecast here
		JSONObject prodjsonobj = (JSONObject) obj;

		// String bearerToken = "PUx5Gt4DZ3ncE7CugwyULGLTy6BMLCXf4XztxZ_LVQ8";
		Response response = RestAssured.given().auth().oauth2(accessToken).contentType(ContentType.JSON)
				.body(prodjsonobj).post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses").then()
				.extract().response();
		response.getBody().prettyPrint();

		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		/*
		 * Map<String, String> id_create = response.jsonPath().getJsonObject("data");
		 * ID=id_create.get("id");
		 */

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		ID = jsonPathEvaluator.get("data.id").toString();
		System.out.println(" id is =>  " + ID);
	}

	@Test(priority = 2)
	public void Update_Address() throws IOException, ParseException {

		// Create json object of JSONParser class to parse the JSON data
		JSONParser jsonparser = new JSONParser();
		// Create object for FileReader class, which help to load and read JSON file
		FileReader reader = new FileReader(".\\TestData\\address_update.json");
		// Returning/assigning to Java Object
		Object obj = jsonparser.parse(reader);
		// Convert Java Object to JSON Object, JSONObject is typecast here
		JSONObject prodjsonobj = (JSONObject) obj;

		// String bearerToken = "PUx5Gt4DZ3ncE7CugwyULGLTy6BMLCXf4XztxZ_LVQ8";
		Response response = RestAssured.given().auth().oauth2(accessToken).contentType(ContentType.JSON)
				.body(prodjsonobj).patch("https://demo.spreecommerce.org/api/v2/storefront/account/addresses/" + ID)
				.then().extract().response();
		response.getBody().prettyPrint();

		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		Map<String, String> default_billing_address = response.jsonPath().getJsonObject("data.attributes");
		String firstName = default_billing_address.get("firstname");
		Assert.assertEquals(firstName, "Minh");

		String lastName = default_billing_address.get("lastname");
		Assert.assertEquals(lastName, "N");

		String addressOne = default_billing_address.get("address1");
		Assert.assertEquals(addressOne, "456 6th St");

		String addressTwo = default_billing_address.get("city");
		Assert.assertEquals(addressTwo, "Austin");
	}

}

package com.spree;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import java.io.FileReader;
import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_An_Address_Using_File {
	
	//Global Variable
	String accessToken;

	@BeforeTest
	public void pre_condition() {
		accessToken = BaseClass.oAuth_Token();
	}
    
	@Test
	public void CreateAddress() throws IOException, ParseException {

		  //Create json object of JSONParser class to parse the JSON data
		  JSONParser jsonparser=new JSONParser();
		  //Create object for FileReader class, which help to load and read JSON file
		  FileReader reader = new FileReader(".\\TestData\\address.json");
		  //Returning/assigning to Java Object
		  Object obj = jsonparser.parse(reader);
		  //Convert Java Object to JSON Object, JSONObject is typecast here
		  JSONObject prodjsonobj = (JSONObject)obj;
		
		//String bearerToken = "cFWLwgbsV-mTZtiYxzkR9YvQQTcE-NvSAhK44CmyVG";
		Response response = RestAssured.given()
				.auth()
				.oauth2(accessToken)
				.contentType(ContentType.JSON)
				.body(prodjsonobj)
				.post("https://demo.spreecommerce.org/api/v2/storefront/account/addresses")
				.then()
				.extract()
				.response();
		response.getBody().prettyPrint();
		

		// Now let us print the body of the message to see what response
		  // we have recieved from the server
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body is =>  " + responseBody);
		  // Status Code Validation
		  int statusCode = response.getStatusCode();
		  System.out.println("Status code is =>  " + statusCode);
		  Assert.assertEquals(200, statusCode);	
 
		  // First get the JsonPath object instance from the Response interface
		 //Assert.assertEquals(responseBody.contains("Komal") /*Expected value*/, true /*Actual Value*/, "Response body contains Abhi");
		  // convert the body into lower case and then do a comparison to ignore casing.
		 //Assert.assertEquals(responseBody.contains("K") /*Expected value*/, true /*Actual Value*/, "Response body contains Dixit");

		 JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		  String fname=jsonPathEvaluator.get("data.attributes.firstname").toString();
		  System.out.println("First Name is =>  " + fname);
		  Assert.assertEquals(fname, "Komal");
		  // VErify that Token Type is Bearer or not
		  String Lname=jsonPathEvaluator.get("data.attributes.lastname").toString();
		  //String ExpTokenType = "Bearer";
		  Assert.assertEquals(Lname, "K");
	}
	
}

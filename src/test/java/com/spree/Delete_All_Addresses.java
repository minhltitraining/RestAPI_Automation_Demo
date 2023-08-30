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

	@Test
	public void deleteAllAddress() {
		String accessToken = BaseClass.oAuth_Token();
		BaseClass.deleteAllAddresses(accessToken);
	}
}

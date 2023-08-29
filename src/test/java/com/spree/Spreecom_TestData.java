package com.spree;

import org.testng.annotations.DataProvider;

public class Spreecom_TestData {
	@DataProvider(name="country_iso")
	public Object[][] iso_name(){
		// Two dimensional object
	    return new Object[][] {
	            {"usa","UNITED STATES","USA"},
	            {"ind","INDIA","IND"},
	            {"pak","PAKISTAN","PAK"},
	            {"gb","UNITED KINGDOM","GBR"},
	            {"afg","AFGHANISTAN","AFG"}
	            };
	}
}

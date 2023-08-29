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
	
	@DataProvider(name="Addresses")
	public Object[][] addresses(){
		return new Object[][] {
			{"Minh1", "N1", "100 1st St", "Dallas", "75001","1", "TX", "US"},
			{"Minh2", "N2", "200 2nd St", "Houston","75002","2", "TX", "US"},
			{"Minh3", "N3", "300 3rd St", "Austin", "75003","3", "TX", "US"},
			{"Minh4", "N4", "400 4th St", "San Antonio", "75004","4", "TX", "US"},
			{"Minh5", "N5", "500 5th St", "College Station", "75005","5", "TX", "US"},
		};
	}
}

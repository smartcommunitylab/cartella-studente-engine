package it.smartcommunitylab.csengine.common;

public enum AddressAttr {
	extendedAddress("extendedAddress"),
	street("street"),
	locality("locality"),
	region("region"),
	postalCode("postalCode"),
	country("country");
	
	public final String label;
	
	private AddressAttr(String label) {
    this.label = label;
	}

}

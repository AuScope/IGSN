package org.csiro.binding.web.response;

public class SampleSummaryResponse {
	
	private String name;
	private String igsn;
	private String logTimeStamp;
	private String landingPage;
	private Integer searchResultCount;
	private Double latitude;
	private Double longitude;
	private String message;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIgsn() {
		return igsn;
	}
	public void setIgsn(String igsn) {
		this.igsn = igsn;
	}
	public String getLogTimeStamp() {
		return logTimeStamp;
	}
	public void setLogTimeStamp(String logTimeStamp) {
		this.logTimeStamp = logTimeStamp;
	}
	public String getLandingPage() {
		return landingPage;
	}
	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public Integer getSearchResultCount() {
		return searchResultCount;
	}
	public void setSearchResultCount(Integer searchResultCount) {
		this.searchResultCount = searchResultCount;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String parseMessage) {
		this.message = parseMessage;
		
	}

	
	

}

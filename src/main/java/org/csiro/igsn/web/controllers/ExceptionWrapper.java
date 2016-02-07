package org.csiro.igsn.web.controllers;

public class ExceptionWrapper {
	private String header;
	private String message;	
	
	public ExceptionWrapper(String header,String message){
		this.setMessage(message);
		this.setHeader(header);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}

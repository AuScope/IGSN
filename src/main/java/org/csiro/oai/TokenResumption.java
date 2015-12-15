package org.csiro.oai;

import java.util.Date;

public class TokenResumption {
	
	private Long completeListSize;
	private int page;
	private Integer cursor;
	private String metadataprefix;
	private String from;
	private String until;
	private String key;
	
	
	
	public Long getCompleteListSize() {
		return completeListSize;
	}
	
	public void setCompleteListSize(Long completeListSize) {
		this.completeListSize = completeListSize;
	}
	
	public Integer getCursor() {
		return cursor;
	}
	
	public void setCursor(Integer cursor) {
		this.cursor = cursor;
	}

	public String getMetadataprefix() {
		return metadataprefix;
	}

	public void setMetadataprefix(String metadataprefix) {
		this.metadataprefix = metadataprefix;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

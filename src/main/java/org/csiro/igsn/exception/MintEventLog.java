package org.csiro.igsn.exception;

public class MintEventLog {

	private static final long serialVersionUID = -1695345647328255048L;
	
	private MintErrorCode code;	
	private String message;
	private String exceptionCause;
	private String sampleId;

	
	
	public MintEventLog(MintErrorCode code,String exceptionCause,String sampleId){
		this.setLog(code,exceptionCause);
		this.setSampleId(sampleId);
	}
	
	public MintEventLog(String sampleId){		
		this.setSampleId(sampleId);
	}
	
	public MintErrorCode getCode(){
		return code;
	}

	public String getExceptionCause() {
		return exceptionCause;
	}


	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public void setLog(MintErrorCode code, String exceptionCause) {
		this.code = code;
		this.message = code.getMessage();
		this.exceptionCause=exceptionCause;
	}

	public String getMessage() {
		return message;
	}
	

}

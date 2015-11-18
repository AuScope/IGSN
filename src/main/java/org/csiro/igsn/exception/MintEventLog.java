package org.csiro.igsn.exception;

public class MintEventLog {

	private static final long serialVersionUID = -1695345647328255048L;
	
	private MintErrorCode mintStatus;
	private DatabaseErrorCode databaseStatus;
	private String mintLog;
	private String databaseLog;
	private String mintExceptionCause;
	private String databaseExceptionCause;
	private String sampleId;
	private String handle;

	
	
	public MintEventLog(MintErrorCode code,String exceptionCause,String sampleId){
		this.setMintLog(code,exceptionCause);
		this.setSampleId(sampleId);
	}
	
	public MintEventLog(String sampleId){		
		this.setSampleId(sampleId);
	}
	
	public MintErrorCode getMintStatus(){
		return mintStatus;
	}
	
	public int getMintStatusCode(){
		return mintStatus.getNumber();
	}
	
	public DatabaseErrorCode getDatabaseStatus(){
		return databaseStatus;
	}
	
	public int getDatabaseStatusCode(){
		return databaseStatus.getNumber();
	}

	public String getMintExceptionCause() {
		return mintExceptionCause;
	}
	
	public String getDatabaseExceptionCause() {
		return databaseExceptionCause;
	}


	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public void setMintLog(MintErrorCode code, String exceptionCause) {
		this.mintStatus = code;
		this.mintLog = code.getMessage();
		this.mintExceptionCause=exceptionCause;
	}
	
	public void setDatabaseLog(DatabaseErrorCode code, String exceptionCause) {
		this.databaseStatus = code;
		this.databaseLog = code.getMessage();
		this.databaseExceptionCause=exceptionCause;
	}

	public String getMintLog() {
		return mintLog;
	}
	
	public String getDatabaseLog() {
		return databaseLog;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}
	

}

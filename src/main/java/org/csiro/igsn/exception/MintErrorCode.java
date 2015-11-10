package org.csiro.igsn.exception;

public enum MintErrorCode {
	
	PREFIX_UNREGISTERED(101),
	MINT_FAILURE(102),
	DATABASE_UPDATE_ERROR(103),
	SUCCESS(200);
	
	
	
	

	private final int number;
 
	 private MintErrorCode(int number) {
	   this.number = number;
	 }
	 
	  
	 public int getNumber() {
	   return number;
	 }
	 
	 
	 public String getMessage() {
		 String message ="";
		 switch(number){
		 	case 101: message="Prefix is unregistered to the user";
		 		break;
		 	case 102: message="Error attempting to Mint from registery";
	 			break;	
		 	case 103: message="Error occurred while updating database";
 				break;
		 	case 200: message="Mint Successful";
				break;	
			default: message="Error not capture, please send a sample of your file to cg-admin@csiro.au";	
 				break;
		 }
		 
		 return message;
	 }
	

}

package org.csiro.igsn.exception;

public enum DatabaseErrorCode {
		
	UPDATE_ERROR(103),
	DUPLICATE_KEY(104),
	UPDATE_SUCCESS(200);
	
	
	
	

	private final int number;
 
	 private DatabaseErrorCode(int number) {
	   this.number = number;
	 }
	 
	  
	 public int getNumber() {
	   return number;
	 }
	 
	 
	 public String getMessage() {
		 String message ="";
		 switch(number){
		 	case 103: message="Error updating the database, please try again later.";
		 		break;
		 	case 104: message="Duplicate key. Please use updated instead of submitted";
	 			break;	
		 	case 200: message="Database successfully updated.";
	 			break;			 	
			default: message="Error not capture, please send a sample of your file to cg-admin@csiro.au";	
 				break;
		 }
		 
		 return message;
	 }
	

}

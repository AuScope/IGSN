package org.csiro.igsn.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NullUtilities {
	
	public static Date parseDateAllowNull(String date) throws ParseException{		
		DateFormat df =new SimpleDateFormat("dd/MMM/yyyy");		
		return (date==null || date.isEmpty())?null:df.parse(date);			
	}
	
	//VT: parse string date YYYY-MM-DD
	public static Date parseDateYYYYMMDDAllowNull(String date) throws ParseException{		
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
		return (date==null || date.isEmpty())?null:df.parse(date);			
	}

}

package br.net.toolbox.android.xospam.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataFormaterHelper {

	
	public String formatData(Long unixTime){
		
		Date date = new Date();
		date.setTime(unixTime);
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		Calendar calAgora = new GregorianCalendar();
		
		String maskDate = "dd/MM/yyyy";
		
		if (calAgora.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))
			maskDate = "HH:mm";
		
		SimpleDateFormat df = new SimpleDateFormat(maskDate);
		
		return df.format(date);
		
	}
	
	
}

package apps.suvadeep.com.expensemanager.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class for all random utility functions.
 */
public final class UtilsCommons {

    /**
     * returns the current timestamp in YYYY-MM-DD HH:MM:SS.sss format
     * @return
     */
    public static String getTimeStamp(){
        return new Timestamp(new Date().getTime()).toString();
    }

    /**
     * returns the current timestamp in yyyyMMddHHmmsss format
     * @return
     */
    public static String getRawTimeStamp(){
       return new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
    }

    /**
     * Returns a string with the current year and month in YYYY-MM format
     * @return
     */
    public static String getCurrentYearMonth() {
        Calendar c = Calendar.getInstance();
        String appendZero = "";
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        if(month<10){
            appendZero = "0";
        }
        String yearMonth = year+"-"+appendZero+month;
        System.out.println(yearMonth);
        return yearMonth;
    }


}

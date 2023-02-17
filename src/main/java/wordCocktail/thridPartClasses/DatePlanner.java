package wordCocktail.thridPartClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public abstract class DatePlanner {
    /**
     * Handles planned dates.
     *
     * @source <a href="https://www.baeldung.com/java-increment-date#calander">baeldung, calendar</a>
     */
    public static String addOneDayCalendar(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        c.add(Calendar.DATE, 1);
        return sdf.format(c.getTime());
    }

    public static Date addDays(Date date, int numPlusDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, numPlusDays);
        return c.getTime();
    }
}

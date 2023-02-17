package wordCocktail.sandBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SandBox {

    /**
     * Tries handling dates with their formats.
     * @throws ParseException
     */
    public static void formatDate() throws ParseException {
//        https://stackoverflow.com/questions/3469507/how-can-i-change-the-date-format-in-java
//                final String OLD_FORMAT = "dd/MM/yyyy";
//                final String OLD_FORMAT = "Fri Feb 17 14:09:05 CET 2023";
        final String OLD_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
//                final String NEW_FORMAT = "yyyy/MM/dd";
        final String NEW_FORMAT = "yyyy.MM.dd.,HH:mm:ss";

//                String oldDateString = "12/08/2010";
        String oldDateString = "Fri Feb 17 14:09:05 CET 2023";
        String newDateString;

//                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.ENGLISH); //https://stackoverflow.com/questions/35722729/java-convert-cet-string-to-date
        Date d = sdf.parse(oldDateString);
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        System.out.println(newDateString);
    }
}

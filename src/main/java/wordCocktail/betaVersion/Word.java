package wordCocktail.betaVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Word {
    private final String wordEng;
    private final String parentDir;
    private Date lastDate;

    public Word(String parentDir, String wordEng) {
        this.wordEng = wordEng;
        this.parentDir = parentDir;
    }

    //Get end of last line of txt as lastDate
    public Date getLastDate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(parentDir + File.separator + wordEng + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("last date")) {
                    String sDate1 = line.substring(11);
                    lastDate = new SimpleDateFormat("yyyy.MM.dd").parse(sDate1);
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return lastDate;
    }
}

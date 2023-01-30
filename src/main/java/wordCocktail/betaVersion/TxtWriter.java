package wordCocktail.betaVersion;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TxtWriter {
    public static void saveTrainingDate(String parentDirPath, String word) {
        String index = "repeated,";
        File fileOld = new File(parentDirPath + File.separator + word + ".txt");
        File fileTemp = new File(parentDirPath + File.separator + word + "Temp" + ".txt");
        try (FileInputStream fis = new FileInputStream(fileOld); //https://mkyong.com/java/how-to-read-utf-8-encoded-data-from-a-file-java
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr);
             FileOutputStream fos = new FileOutputStream(fileTemp); //https://mkyong.com/java/how-to-read-utf-8-encoded-data-from-a-file-java
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.append(line);
                writer.newLine();
            }
            writer.append(index)
                    .append(DateTimeFormatter
                            .ofPattern("yyyy.MM.dd.,HH:mm:ss")
                            .format(LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean delFlag = fileOld.delete();
        boolean renameFlag = fileTemp.renameTo(new File(parentDirPath + File.separator + word + ".txt"));
        if (delFlag && renameFlag){
            System.out.println();
        }
    }
}

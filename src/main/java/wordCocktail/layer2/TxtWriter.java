package wordCocktail.layer2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TxtWriter {
    /**
     * Makes a copy of every line of the original txt file in a temporal file then writes the practice date and time
     * into the training diary after the "repeated" index. After that, deletes the original file and rename
     * the temporal file to the original name. //TODO any plainer solution?
     *
     * @param parentDirPath directory path where the words are located
     * @param word          actual word
     * @throws IOException
     */
    public static void saveTrainingDate(String parentDirPath, String word) throws IOException {
        File fileOld = new File(parentDirPath + File.separator + word + ".txt");
        File fileTemp = new File(parentDirPath + File.separator + word + "Temp" + ".txt");
        try (FileInputStream fis = new FileInputStream(fileOld); //https://mkyong.com/java/how-to-read-utf-8-encoded-data-from-a-file-java
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr);
             FileOutputStream fos = new FileOutputStream(fileTemp);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.append(line);
                writer.newLine();
            }
            writeDateAndTime(writer);
        }
        FileChanger.changeTempToOriginal(fileTemp, fileOld, "");
    }

    /**
     * Writes the practice date and time into the training diary after the "repeated" index.
     *
     * @param writer BufferedWriter resource from "try" block
     **/
    private static void writeDateAndTime(BufferedWriter writer) throws IOException {
        writer.append("repeated,").append(DateTimeFormatter
                .ofPattern("yyyy.MM.dd.,HH:mm:ss")
                .format(LocalDateTime.now()));
    }
}

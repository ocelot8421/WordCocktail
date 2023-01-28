package betaVersion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TxtCreator {

    //Creates txt file for every new words (hun/eng) contains when the word is started to learn, when needed to repeat, so on)
    public static void createDataTxt(String[] csvRow, Path dirDated) {
        String savingDateIndex = "saved,";
        String source = dirDated.getName(2) + "," +
                dirDated.getName(3) + "," +
                dirDated.getName(4);
        File fileEng = new File(dirDated + File.separator + csvRow[2] + ".txt");
        writeStarterDate(fileEng, savingDateIndex, source, csvRow[2]);
        File fileHun = new File(dirDated + File.separator + csvRow[3] + ".txt");
        writeStarterDate(fileHun, savingDateIndex, source, csvRow[3]);
    }

    private static void writeStarterDate(File fileWord, String savingDateIndex, String source, String word) {
        try (FileWriter writer = new FileWriter(fileWord, StandardCharsets.UTF_8)) {
            writer.write("word, " + word + "\n" +
                    "source," + source + "\n" +
                    savingDateIndex + DateTimeFormatter.ofPattern("yyyy.MM.dd.,HH:mm:ss").format(LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

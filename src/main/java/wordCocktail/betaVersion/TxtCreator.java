package wordCocktail.betaVersion;

import wordCocktail.encoding.DecodeText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TxtCreator {

    //Creates txt file for every new words (hun/eng) contains when the word is started to learn, when needed to repeat, so on)
    public static void createDataTxt(String[] csvRow, Path dirDated) throws IOException {
        String savingDateIndex = "saved,";
        String source = dirDated.getName(2) + "," +
                dirDated.getName(3) + "," +
                dirDated.getName(4);
        String engWord = DecodeText.decodeText(csvRow[2], "UTF-8");
        File fileEng = new File(dirDated + File.separator + engWord + ".txt");
        writeStarterDate(fileEng, savingDateIndex, source, engWord);
        String hunWord = DecodeText.decodeText(csvRow[3], "UTF-8");
        File fileHun = new File(dirDated + File.separator + hunWord + ".txt");
        writeStarterDate(fileHun, savingDateIndex, source, hunWord);
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

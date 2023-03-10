package wordCocktail.layer2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TxtCreator {

    private static final int colHun = 2; //Index of column of hungarian words
    private static final int colEng = 3; //Index of column of english words

    /**
     * Creates a txt file for every new word (hun/eng) that contains when the word is started to learn, when needed to repeat, so on)
     *
     * @param csvRow    contains 4 String data: "angol", "magyar", english word, hungarian word.
     * @param directory contains renamed png files and links.
     * @throws IOException
     **/
    public static void createTXT(String[] csvRow, Path directory) throws IOException {
        Path parent = directory.getParent();
        String sourceDesignation = parent.getParent().getFileName() + "," +
                parent.getFileName() +","+
                directory.getFileName();
        createTxtFile(csvRow, directory, sourceDesignation, colHun, colEng);
        createTxtFile(csvRow, directory, sourceDesignation, colEng, colHun);
    }

    /**
     * Makes a txt file in the directory and names it from CSV
     *
     * @param csvRow        CSV contains english and hungarian words. Imported from Google Translate. //TODO the google translate part should be README content
     * @param dir           directory contains png files, links.
     * @param source        source of the word, taken from the directory path. //TODO from file content named "source"
     * @param numCol        index of the column where the word used for renaming is found.
     * @param numColForeign translation of the word
     * @throws IOException
     **/
    private static void createTxtFile(String[] csvRow, Path dir, String source, int numCol, int numColForeign) throws IOException {
        String word = DecodeText.decodeText(csvRow[numCol], "UTF-8");
        String wordForeign = DecodeText.decodeText(csvRow[numColForeign], "UTF-8");
        File file = new File(dir + File.separator + word + ".txt");
        writeStarterDate(file, source, word, wordForeign);
    }

    /**
     * Writes starter information in the first three lines of txt file: word-pair, source, date of saving.
     *
     * @param file        the txt file.
     * @param source      source of the word, taken from the parent directory path.
     * @param word        english or hungarian word.
     * @param wordForeign translation of the word
     * @throws IOException
     */
    private static void writeStarterDate(File file, String source, String word, String wordForeign) throws IOException {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write("word, " + word + " - " + wordForeign + "\n" +
                    "source, " + source + "\n" +
                    "saved, " + DateTimeFormatter.ofPattern("yyyy.MM.dd.,HH:mm:ss").format(LocalDateTime.now()) +
                    "\nlevel,0"
            );
        }
    }
}

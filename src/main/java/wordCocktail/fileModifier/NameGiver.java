package wordCocktail.fileModifier;//Changing name of new screenshots into english word.
//Making links for them and changing name into hungarian translation.

import wordCocktail.txtModifiers.TxtCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static wordCocktail.questioner.Questioner.askForUTF8Answer;


public class NameGiver {

    //Renames new screenshots from the videos
    public static void renameScreenshots() throws IOException {

        //Asks for the source directory
        String path = askForUTF8Answer("Where are the new screenshots? \n" +
                "(For example: C:/words/english/...) \n");
        Path pathRelevant = Paths.get(path);
        int pathLength = pathRelevant.getNameCount();
        List<String[]> contentCSV = new ArrayList<>();

        //Defines the CSV that contains English-Hungarian words
        File pathNewImages = new File(path);
        for (File listFile : Objects.requireNonNull(pathNewImages.listFiles())) {
            if (receiveExtension(listFile).equals("csv")) { //Receive the part of the file name after dot(".")
                contentCSV = receiveCsvAsList(listFile);
            }
        }

        //Makes new directory named the original directory append actual date and time
        //Make a copy of png files renamed by the english word from csv
        //Make a link for png files
        //Make a txt file named hun/eng word
        int i = contentCSV.size() - 1;
        Path newDirectoryForRenamedFiles = Paths.get(pathNewImages.getParent() +
                File.separator +
                pathRelevant.subpath(pathLength - 1, pathLength) +
                " -- " +
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now()));
        Files.createDirectory(newDirectoryForRenamedFiles);
        for (File listFile : Objects.requireNonNull(pathNewImages.listFiles())) {
            if (receiveExtension(listFile).equals("png")) {
                String nameEnglish = contentCSV.get(i)[2];
                String nameHungarian = DecodeText.decodeText(contentCSV.get(i)[3], "UTF-8");
                Path oldName = Paths.get(listFile.getAbsolutePath());
                Path engName = Paths.get(newDirectoryForRenamedFiles + File.separator + nameEnglish + ".png");
                Files.copy(oldName, engName, REPLACE_EXISTING);
                LinkCreator.createLink(String.valueOf(engName), newDirectoryForRenamedFiles + File.separator + nameHungarian + ".lnk");
                TxtCreator.createTXT(contentCSV.get(i), newDirectoryForRenamedFiles);
                i--;
            }
        }
    }

    //Receives all data from CSV (columns: "angol", "magyar", english word, hungarian word)
    private static List<String[]> receiveCsvAsList(File listFile) throws IOException {
        List<String[]> contentCSV = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(listFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("\"", "");
                String[] row = line.split(",");
                contentCSV.add(row);
            }
        }
        return contentCSV;
    }

    //Receive the part of the file name after dot(".")
    private static String receiveExtension(File listFile) {
        String name = listFile.getName();
        int indexExtension = name.lastIndexOf(".");
        return name.substring(indexExtension + 1);
    }
}

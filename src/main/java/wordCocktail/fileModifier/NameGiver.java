package wordCocktail.fileModifier;//Changing name of new screenshots into english word.
//Making links for them and changing name into hungarian translation.

import wordCocktail.betaVersion.TxtCreator;
import wordCocktail.encoding.DecodeText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    //Rename new screenshots from the videos
    public static void renameScreenshots() throws IOException {
        String path = askForUTF8Answer("Where are the new screenshots? \n" + //Ask for the source directory
                "(For example: C:/words/english/...) \n");
//        String newDirectory = new String(askForUt8Answer("How do you want to name the new directory?? \n" + //Ask for the source directory
//                "(For example: week 0 001122-002233)"), StandardCharsets.UTF_8);
        Path pathRelevant = Paths.get(path);
        int pathLength = pathRelevant.getNameCount();
        String newDirectory = pathRelevant.subpath(pathLength - 1, pathLength).toString();
        List<String[]> contentCSV = new ArrayList<>();

        //Define the csv with english-hungarian words
        File pathNewImages = new File(path);
        File[] dirContent = pathNewImages.listFiles();
        for (File listFile : Objects.requireNonNull(dirContent)) {
            if (receiveExtension(listFile).equals("csv")) { //Receive the part of the file name after dot(".")
                contentCSV = receiveCsvAsList(listFile);
            }
        }

        //Make new directory named the actual data and time
        int i = contentCSV.size() - 1;
        Path dirDated = Paths.get(pathNewImages.getParent() +
                File.separator +
                newDirectory + " -- " +
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now()));
        try {
            Files.createDirectory(dirDated);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Make a copy of png files renamed by the english word from csv
        //Make a link for png files
        //Make a txt file named hun/eng word
        for (File listFile : Objects.requireNonNull(dirContent)) {
            if (receiveExtension(listFile).equals("png")) { //Receive the part of the file name after dot(".")
                String nameEnglish = contentCSV.get(i)[2];
                String nameHungarian = DecodeText.decodeText(contentCSV.get(i)[3], "UTF-8");
                Path oldName = Paths.get(listFile.getAbsolutePath());
                Path engName = Paths.get(dirDated + File.separator + nameEnglish + ".png");
                try {
                    Files.copy(oldName, engName, REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //Make a link for png files by a third part class AKA ShortcutFactory
                LinkCreator.createLink(String.valueOf(engName), dirDated + File.separator + nameHungarian + ".lnk");
                //Make a txt file named hun/eng word
                TxtCreator.createDataTxt(contentCSV.get(i), dirDated);
                i--;
            }
        }
    }

    //Receive all data from csv (columns: "angol", "magyar", english words, hungarian words)
    private static List<String[]> receiveCsvAsList(File listFile) {
        List<String[]> contentCSV = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(listFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                     line = line.replace("\"", "");
                String[] row = line.split(",");
                contentCSV.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

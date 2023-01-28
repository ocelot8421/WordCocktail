package fileModifier;//Changing name of new screenshots into english word.
//Making links for them and changing name into hungarian translation.

import betaVersion.TxtCreator;

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
import static questioner.Questioner.askDirectoryLocation;

public class NameGiver {

    //Rename new screenshots from the videos
    public static void renameScreenshots() {
        String path = askDirectoryLocation("Where are the new screenshots? \n" + //Ask for the source directory
                "(For example: C:/words/english/...)");
        File pathNewImages = new File(path);
        Path pathRelevant = Path.of(path);
        int pathLength = pathRelevant.getNameCount();
        String newDirectory = pathRelevant.subpath(pathLength - 1, pathLength).toString();
        List<String[]> contentCSV = new ArrayList<>();
        File[] dirContent = pathNewImages.listFiles();

        //Define the csv with english-hungarian words
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
                String nameEnglish = contentCSV.get(i)[2]; //TODO "" filter
                String nameHungarian = contentCSV.get(i)[3];
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

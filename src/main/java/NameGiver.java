//Changing name of new screenshots into english word.
//Making links for them and changing name into hungarian translation.

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

    //Rename new screenshots from the video
    public void renameScreenshots() {

        //Ask for the source directory
        String p = askDirectoryLocation("Where are the new screenshots? \n(For example: C:/words/english/...)");
        File pathNewImages = new File(p);
        List<String[]> contentCSV = new ArrayList<>();
        File[] dirContent = pathNewImages.listFiles();

        //Define the csv with english-hungarian words
        for (File listFile : Objects.requireNonNull(dirContent)) {
            if (receiveExtension(listFile).equals("csv")) {
                contentCSV = receiveCSVData(listFile);
            }
        }

        //Make new directory named the actual data and time
        int i = contentCSV.size() - 1;
        Path dirDated = Paths.get(pathNewImages.getParent() +
                File.separator +
                DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now()));
        try {
            Files.createDirectory(dirDated);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Make a copy of png files renamed by the english word from csv
        for (File listFile : Objects.requireNonNull(dirContent)) {
            if (receiveExtension(listFile).equals("png")) {
                Path oldName = Paths.get(listFile.getAbsolutePath());
                Path imgRenamed = Paths.get(
                        dirDated +
                                File.separator +
                                contentCSV.get(i)[2] +
                                ".png");
                System.out.println(imgRenamed);
                try {
                    Files.copy(oldName, imgRenamed, REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                i--;
            }
        }
    }

    //Receive all data from csv (columns: "angol", "magyar", english words, hungarian words)
    private List<String[]> receiveCSVData(File listFile) {
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
    private String receiveExtension(File listFile) {
        String name = listFile.getName();
        int indexExtension = name.lastIndexOf(".");
        return name.substring(indexExtension + 1);
    }
}

//Changing name of new screenshots into english word.
//Making links for them and changing name into hungarian translation.

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static questioner.Questioner.askDirectoryLocation;

public class NameGiver { //TODO

    //Rename new screenshots from video
    public void renameScreenshots() {
        String p = askDirectoryLocation("Where are the new screenshots? \n(For example: C:/words/english/...)");
        File pathNewImages = new File(p);
        List<String[]> contentCSV = new ArrayList<>();
        File[] dirContent = pathNewImages.listFiles();
        for (File listFile : Objects.requireNonNull(dirContent)) {
            if (receiveExtension(listFile).equals("csv")) {
                contentCSV = receiveCSVData(listFile);
            }
        }

        int i = contentCSV.size() - 1;
        for (File listFile : Objects.requireNonNull(dirContent)) {
            if (receiveExtension(listFile).equals("png")) {
                File imgRenamed = new File(listFile.getParentFile().getParentFile()
                        + File.separator
                        + new File(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now()))
                        + File.separator
                        + contentCSV.get(i)[2]
                        + ".png");
                try {
                    FileUtils.copyFile(listFile, imgRenamed);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                i--;
            }
        }
    }

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

    private String receiveExtension(File listFile) {
        String name = listFile.getName();
        int indexExtension = name.lastIndexOf(".");
        return name.substring(indexExtension + 1);
    }
}

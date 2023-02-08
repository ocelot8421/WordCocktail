package wordCocktail;

import wordCocktail.questioner.Questioner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {

    public static void analyze() throws IOException, ParseException {
        String directory = Questioner.askForUTF8Answer("\"Where are the words to analyze?\n" +
                "(For example: C:/words/english/...)");
        System.out.println("Collecting txt files...");
        List<File> txtList = collectTxtFiles(directory);
        System.out.println("Analyzing...");
        for (File file : txtList) {
            analyzeTxtFiles(directory, file);
        }
    }

    /**
     * Collects all txt files from given directory
     *
     * @param directory directory contains txt files
     * @return list of txt files
     */
    private static List<File> collectTxtFiles(String directory) {
        return Arrays.stream(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(e -> e.getName().endsWith(".txt"))
                .collect(Collectors.toList());
    }

    /**
     * Analyzes the lines of practice diary txt file.
     *
     * @param directory path of directory contains txt files
     * @param fileTxt   txt file
     * @throws IOException
     */
    private static void analyzeTxtFiles(String directory, File fileTxt) throws IOException, ParseException {
        File fileTemp = new File(directory + File.separator + "Temp" + ".txt");
        try (FileInputStream fis = new FileInputStream(fileTxt); //https://mkyong.com/java/how-to-read-utf-8-encoded-data-from-a-file-java
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr);
             FileOutputStream fos = new FileOutputStream(fileTemp);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            String line;
            int level = 0;
            boolean repeated = false;
            List<List<Date>> dateArray2d = new ArrayList<>();
            List<Date> dateArray1d = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("repeated,")) {
                    repeated = true;
                    String dateString = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
                    Date date = new SimpleDateFormat("yyyy.MM.dd.").parse(dateString);
                    if (dateArray1d.isEmpty()) {
                        dateArray1d.add(date);
                    } else {
                        if (dateArray1d.contains(date)) {
                            dateArray1d.add(date);
                        } else {
                            dateArray2d.add(new ArrayList<>(dateArray1d));
                            dateArray1d.clear();
                            dateArray1d.add(date);
                        }
                    }
                }
//                writer.append(line); //TODO write new txt file with word-level
//                writer.newLine();
            }
            dateArray2d.add(dateArray1d);
            if (repeated) {
                int sizeDateArray2d = dateArray2d.size();
                level = dateArray2d.get(sizeDateArray2d - 1).size();
            }
            System.out.println(level);
            String fileName = fileTxt.getName();
            boolean delFlag = fileTxt.delete();
            boolean renameFlag = fileTemp.renameTo(new File(fileName));
            if (delFlag && renameFlag) {
                System.out.println("Analyzed: " + fileName);
            }
        }
    }
}

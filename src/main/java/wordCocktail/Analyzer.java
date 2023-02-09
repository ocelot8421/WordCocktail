package wordCocktail;

import wordCocktail.betaVersion.Word;
import wordCocktail.questioner.Questioner;
import wordCocktail.txtModifiers.FileChanger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analyzes learning progress by counting the number of times a word has been practiced.
 */
public class Analyzer {

    /**
     * Looks for practice logs in given directory, collects and analyzes them.
     *
     * @throws IOException
     * @throws ParseException
     */
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
     * Analyzes the lines of practice diary txt file by collecting the same practice dates into
     * a {@code List<Date>} then these lists are put into the {@code List<List<Date>>} per word.
     *
     * @param directory path of directory contains txt files
     * @param fileTxt   txt file
     * @throws IOException
     */
    private static void analyzeTxtFiles(String directory, File fileTxt) throws IOException, ParseException {
        File fileTemp = new File(directory + File.separator + fileTxt.getName() + "Temp" + ".txt");
        try (FileInputStream fis = new FileInputStream(fileTxt); //https://mkyong.com/java/how-to-read-utf-8-encoded-data-from-a-file-java
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr);
             FileOutputStream fos = new FileOutputStream(fileTemp);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            String line;
            int level = 0;
            boolean repeated = false;
            List<List<Date>> dateList2d = new ArrayList<>();
            List<Date> dateList1d = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                if (line.startsWith("repeated,")) {
                    repeated = true;
                    String dateString = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
                    Date date = new SimpleDateFormat("yyyy.MM.dd.").parse(dateString);
                    separateDate(date, dateList1d, dateList2d);
                }
            }
            dateList2d.add(dateList1d);
            if (repeated) {
                int sizeDateArray2d = dateList2d.size();
                level = dateList2d.get(sizeDateArray2d - 1).size();
            }
            writer.write("level," + level);
            writer.newLine();
            System.out.println("Updated: " + fileTxt.getName() + "\n Level: " + level);
        }
        FileChanger.changeTempToOriginal(fileTemp, fileTxt, "");
    }

    /**
     * Separates dates and takes them into lists.
     *
     * @param date       {@code Date} format of practice date
     * @param dateList1d {@code List<Date>} contains the same dates
     * @param dateList2d {@code List<List<Date>>} contains lists of dates per word
     */
    private static void separateDate(Date date, List<Date> dateList1d, List<List<Date>> dateList2d) {
        if (dateList1d.isEmpty()) {
            dateList1d.add(date);
        } else {
            if (dateList1d.contains(date)) {
                dateList1d.add(date);
            } else {
                dateList2d.add(new ArrayList<>(dateList1d));
                dateList1d.clear();
                dateList1d.add(date);
            }
        }
    }


}

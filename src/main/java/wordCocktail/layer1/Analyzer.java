package wordCocktail.layer1;

import wordCocktail.layer2.FileChanger;
import wordCocktail.layer2.Word;
import wordCocktail.thridPartClasses.DatePlanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analyzes learning progress by counting the number of times a word has been practiced.
 */
public class Analyzer {

    private static final int DIFFICULTY = 10;
    private static final int DAYS = 14;


    /**
     * @param directory
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static List<List<Word>> analyzeAndReceiveDifficultyGroups(String directory) throws IOException, ParseException {
        List<Word> words = analyzeDirectory(directory);
        List<List<Word>> wordGroups = new ArrayList<>();
        for (int i = 0; i < DIFFICULTY; i++) {
            int finalI = i;
            wordGroups.add(
                    words.stream()
                            .filter(e -> e.getLevel() == finalI)
                            .collect(Collectors.toList())
            );
        }
        return wordGroups;
    }

    public static List<List<Word>> receiveDateGroups(String directory) throws IOException, ParseException {
        List<Word> words = analyzeDirectory(directory);
        List<List<Word>> group = new ArrayList<>();
        Date today = Calendar.getInstance().getTime();
        for (int i = 0; i < DAYS; i++) {
            Date groupDate = DatePlanner.addDays(today, i);
            Date groupDateNext = DatePlanner.addDays(today, i + 1);
            group.add(
                    words.stream()
                            .filter(e ->
                                    e.getNearestPlannedExercise().after(groupDate) &&
                                            e.getNearestPlannedExercise().before(groupDateNext))
                            .collect(Collectors.toList())
            );
        }
        return group;
    }

    /**
     * Looks for practice logs in given directory, collects and analyzes them.
     *
     * @throws IOException
     * @throws ParseException
     */
    public static List<Word> analyzeDirectory(String directoryPath) throws IOException, ParseException {
        System.out.println("Collecting txt files...");
        List<File> txtList = collectTxtFiles(directoryPath);
        System.out.println("Analyzing...");
        List<Word> words = new ArrayList<>();
        for (File fileTxt : txtList) {
            words.add(analyzeTxtFile(directoryPath, fileTxt));
        }
        return words;
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
    public static Word analyzeTxtFile(String directory, File fileTxt) throws IOException, ParseException {
        Word wordAnalyzed = new Word(fileTxt);
        wordAnalyzed.setParentDir(new File(directory));
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
            Date date;
            Date nearestPlannedDateFromTxt = new Date();
            List<Date> dateList1d = new ArrayList<>();
            List<List<Date>> dateList2d = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                int indexAfterFirstComma = line.indexOf(',') + 1;
                switch (line.substring(0, indexAfterFirstComma)) {
                    case "word,":
                        wordAnalyzed.setWord(line.substring(indexAfterFirstComma));
                        break;
                    case "repeated,":
                        repeated = true;
                        date = receiveDateFromLine(line);
                        separateDate(date, dateList1d, dateList2d);
                        break;
                    case "source,":
                        wordAnalyzed.setSource(line.substring(indexAfterFirstComma));
                        break;
                    case "planned,":
                        nearestPlannedDateFromTxt = new SimpleDateFormat("yyyy.MM.dd.,HH:mm:ss").parse(line.substring(indexAfterFirstComma)); //TODO too long line
                        break;
                    case "saved,":
                        date = receiveDateFromLine(line);
                        wordAnalyzed.setSavedDate(date);
                        wordAnalyzed.setPlannedDate();
                        break;
                    case "level,":
                        wordAnalyzed.setLevel(Integer.parseInt(line.substring(indexAfterFirstComma)));
                        break;
                }
            }
            dateList2d.add(dateList1d);
            if (repeated) {
                int sizeDateArray2d = dateList2d.size();
                level = dateList2d.get(sizeDateArray2d - 1).size();
            }
            if (wordAnalyzed.getLevel() != level) {
                wordAnalyzed.setLevel(level);
                writer.write("level," + level);
                writer.newLine();
            }
            writePlannedDate(writer, nearestPlannedDateFromTxt, wordAnalyzed);

            System.out.println("Updated: " + fileTxt.getName() + "\n Level: " + level);
            wordAnalyzed.setLevel(level);
        }
        FileChanger.changeTempToOriginal(fileTemp, fileTxt, "");
        return wordAnalyzed;
    }

    /**
     * @source <a href="https://stackoverflow.com/questions/3469507/how-can-i-change-the-date-format-in-java">stackoverflow, date-format </a>
     * @source <a href="https://stackoverflow.com/questions/35722729/java-convert-cet-string-to-date">stackoverflow, cet </a>
     */
    private static void writePlannedDate(BufferedWriter writer, Date nearestPlannedDateFromTxt, Word wordAnalyzed) throws IOException {
        if (nearestPlannedDateFromTxt.getTime() != wordAnalyzed.getNearestPlannedExercise().getTime()) {
            nearestPlannedDateFromTxt = wordAnalyzed.getNearestPlannedExercise();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            sdf.applyPattern("yyyy.MM.dd.,HH:mm:ss");
            writer.write("planned," + sdf.format(nearestPlannedDateFromTxt));
            writer.newLine();
        }
    }

    /**
     * For example:  repeated,2023.01.30.,23:27:56 -> 2023.01.30.
     *
     * @param line string line from txt file
     * @return date that was written after the index expression.
     * @throws ParseException
     */
    private static Date receiveDateFromLine(String line) throws ParseException {
        String dateString = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
        return new SimpleDateFormat("yyyy.MM.dd.").parse(dateString);
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

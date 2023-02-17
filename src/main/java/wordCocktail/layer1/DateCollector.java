package wordCocktail.layer1;

import wordCocktail.layer2.Word;
import wordCocktail.thridPartClasses.DatePlanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateCollector {

    public static void collectByDate(boolean deletePrevious, String directory) throws IOException, ParseException {
        List<List<Word>> groups = Analyzer.receiveDateGroups(directory);
        Path datesDir = createGroupDir(groups);

        String dateIDDirPrefix = "date_";
        Date today = Calendar.getInstance().getTime();
        String dateIDDirPostfix;
        for (int i = 0; i < groups.size(); i++) {
            dateIDDirPostfix = formatPrettyDate(today, i);
            Path dateIDir = Paths.get(datesDir + File.separator + dateIDDirPrefix + dateIDDirPostfix);
            if (!Files.exists(dateIDir)) Files.createDirectory(dateIDir);
            List<Word> groupI = groups.get(i);
            for (Word w : groupI) {
                Path txtPath = w.getFileTxt().toPath();
                Path txtPathNew = Paths.get(dateIDir + File.separator + w.getFileTxt().getName());
                DifficultyCollector.copyTxt(w, txtPath, txtPathNew, datesDir, deletePrevious);

                Path pngPath = w.getFilePng().toPath();
                Path pngPathNew = Paths.get(dateIDir + File.separator + w.getFilePng().getName());
                DifficultyCollector.copyNotFiles(w, pngPath, pngPathNew, datesDir, deletePrevious);

                Path lnkPath = w.getFileLink().toPath();
                Path lnkPathNew = Paths.get(dateIDir + File.separator + w.getFileLink().getName());
                DifficultyCollector.copyNotFiles(w, lnkPath, lnkPathNew, datesDir, deletePrevious);

            }
        }
    }

    /**
     * Makes a prettier date format.
     *
     * @param today
     * @param i     number of days after today
     * @return yyyy.MM.dd., HH:mm:ss
     * @throws ParseException
     * @source <a href="https://stackoverflow.com/questions/35722729/java-convert-cet-string-to-date">stackoverflow</a>
     */
    private static String formatPrettyDate(Date today, int i) {
        final String OLD_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.ENGLISH);
        final String NEW_FORMAT = "yyyy-MM-dd-EEE";
        sdf.applyPattern(NEW_FORMAT);
        Date date = DatePlanner.addDays(today, i);
        return sdf.format(date);
    }

    private static Path createGroupDir(List<List<Word>> groups) throws IOException {
        String groupsDirName = "dates";
        File parentDir = receiveParentDirectory(groups);
        File originalDir = parentDir.getParentFile();
        originalDir = originalDir.getName().equals(groupsDirName) ? originalDir.getParentFile() : originalDir;
        Path groupsDir = Paths.get(originalDir + File.separator + groupsDirName);
        if (!Files.exists(groupsDir)) Files.createDirectory(groupsDir);
        return groupsDir;
    }

    /**
     * Gets the parent directory by retrieving the first Word element in the first non-empty sublist.
     *
     * @param groups list of sublist contains
     * @return directory will contain "groups" directory
     */
    private static File receiveParentDirectory(List<List<Word>> groups) {
        int n = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (!groups.get(i).isEmpty()) {
                n = i;
                break;
            }
        }
        return groups.get(n).get(0).getParentDir();
    }
}

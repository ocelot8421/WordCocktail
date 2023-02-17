package wordCocktail.layer2;

import java.io.File;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wordCocktail.thridPartClasses.DatePlanner.addDays;

/**
 * Instance of word that is used Analyzer and DifficultyCollector class.
 */
public class Word {
    private String word;
    private String objectName;
    private int level;
    private File parentDir;
    private File fileTxt;
    private String fileTxtName;
    private File fileLink;
    private File filePng;
    private Date savedDate;
    private String source;
    private final List<Date> plannedDate = new ArrayList<>();
    private Date nearestPlannedExercise;

    /**
     * @param fileTxt The diary where the dates are saved.
     */
    public Word(File fileTxt) {
        this.fileTxt = fileTxt;
        this.parentDir = fileTxt.getParentFile();
        this.objectName = fileTxt.getName().substring(0, fileTxt.getName().indexOf('.'));
        this.filePng = new File(parentDir + File.separator + objectName + ".png");
        this.fileLink = new File(parentDir + File.separator + objectName + ".lnk");
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public File getFileTxt() {
        return fileTxt;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setFileTxt(File fileTxt) {
        this.fileTxt = fileTxt;
    }

    public File getFileLink() {
        return fileLink;
    }

    public void setFileLink(File fileLink) {
        this.fileLink = fileLink;
    }

    public File getFilePng() {
        return filePng;
    }

    public void setFilePng(File filePng) {
        this.filePng = filePng;
    }

    public Date getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(Date savedDate) {
        this.savedDate = savedDate;
    }

    public File getParentDir() {
        return parentDir;
    }

    public void setParentDir(File parentDir) {
        this.parentDir = parentDir;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFileTxtName() {
        return fileTxtName;
    }

    public void setFileTxtName(String fileTxtName) {
        this.fileTxtName = fileTxtName;
    }

    public void setPlannedDate() {
        Instant today = ZonedDateTime.now().toInstant();
        Date date;
        Date dateNext;
        this.plannedDate.add(savedDate);
        for (int i = 0; i < 3; i++) {
            date = addDays(savedDate, (int) Math.pow(2, i));
            dateNext = addDays(savedDate, (int) Math.pow(2, i + 1));
            this.plannedDate.add(date);
            searchDateToRepeat(date, today, dateNext);
        }
        for (int j = 3; j < 9; j++) {
            date = addDays(savedDate, (int) Math.pow(2, j) / 8 * 7);
            dateNext = addDays(savedDate, (int) Math.pow(2, j + 1) / 8 * 7);
            this.plannedDate.add(date);
            searchDateToRepeat(date, today, dateNext);
        }
    }

    /**
     * Looks for date when the word is needed to be repeating.
     */
    private void searchDateToRepeat(Date date, Instant today, Date dateNext) {
        boolean isLastDay = today.isAfter(date.toInstant());
        boolean isNextDay = today.isBefore(dateNext.toInstant());
        if (isLastDay && isNextDay)
            this.nearestPlannedExercise = dateNext;
    }

    public List<Date> getPlannedDate() {
        return plannedDate;
    }

    public Date getNearestPlannedExercise() {
        return nearestPlannedExercise;
    }

    public String printPlannedDates() {
        StringBuilder strB = new StringBuilder();
        plannedDate.forEach(date -> strB.append("\n").append(date));
        return strB.toString();
    }

    @Override
    public String toString() {
        return "\nWord{" +
                "word='" + word + '\'' +
                ", objectName='" + objectName + '\'' +
                ", level=" + level +
                ",\n parentDir=" + parentDir +
                ",\n fileTxt=" + fileTxt +
                ",\n fileTxtName='" + fileTxtName + '\'' +
                ",\n fileLink=" + fileLink +
                ",\n filePng=" + filePng +
                ",\n savedDate=" + savedDate +
                ", source='" + source + '\'' +
                ",\n   plannedDate=" + printPlannedDates() +
                ",\n nearestPlannedExercise=" + nearestPlannedExercise +
                '}';
    }
}

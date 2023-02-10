package wordCocktail.fileModifier;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Word { //TODO needed?
    private String word;
    private String objectName;
    private int level;
    private File parentDir;
    private File fileTxt;
    private String fileTxtName;
    private File fileLink;
    private File filePng;
    private Date saved;
    private String source;

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

    public Date getSaved() {
        return saved;
    }

    public void setSaved(Date saved) {
        this.saved = saved;
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

    public void setFileTxtName(String filrTxtName) {
        this.fileTxtName = filrTxtName;
    }

    @Override
    public String toString() {
        return "\nWord{" +
                "word='" + word + '\'' +
                ", level=" + level +
                ", fileTxt=" + fileTxt +
                ", fileLink=" + fileLink +
                ", filePng=" + filePng +
                ", saved=" + saved +
                '}';
    }
}

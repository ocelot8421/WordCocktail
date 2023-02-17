package wordCocktail.layer1;

import wordCocktail.layer2.Word;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class DifficultyCollector {

    /**
     * @param deletePrevious
     * @param directory
     * @throws IOException
     * @throws ParseException
     * @see DateCollector#collectByDate(boolean, String)
     */
    public static void collectByDifficulty(boolean deletePrevious, String directory) throws IOException, ParseException {
        String levelsDirName = "levels";
        String levelIDirPrefix = "level_";
        List<List<Word>> groups = Analyzer.analyzeAndReceiveDifficultyGroups(directory);

        //Receive the first not empty sublist.
        int n = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (!groups.get(i).isEmpty()) {
                n = i;
                break;
            }
        }

        File parentFile = groups.get(n).get(0).getFileTxt().getParentFile();
        File originalDir = parentFile.getParentFile();
        if (parentFile.getParentFile().getName().equals(levelsDirName))
            originalDir = originalDir.getParentFile();
        Path levelsDir = Paths.get(originalDir + File.separator + levelsDirName);
        if (!Files.exists(levelsDir))
            Files.createDirectory(levelsDir);
        for (int i = 0; i < groups.size(); i++) {
            Path levelIDir = Paths.get(levelsDir + File.separator + levelIDirPrefix + i);
            if (!Files.exists(levelIDir))
                Files.createDirectory(levelIDir);
            List<Word> groupI = groups.get(i);
            for (Word w : groupI) {
                Path txtPath = w.getFileTxt().toPath();
                Path txtPathNew = Paths.get(levelIDir + File.separator + w.getFileTxt().getName());
                copyTxt(w, txtPath, txtPathNew, levelIDir, deletePrevious);

                Path pngPath = w.getFilePng().toPath();
                Path pngPathNew = Paths.get(levelIDir + File.separator + w.getFilePng().getName());
                copyNotFiles(w, pngPath, pngPathNew, levelIDir, deletePrevious);

                Path lnkPath = w.getFileLink().toPath();
                Path lnkPathNew = Paths.get(levelIDir + File.separator + w.getFileLink().getName());
                copyNotFiles(w, lnkPath, lnkPathNew, levelIDir, deletePrevious);

            }
        }
    }

    public static void copyTxt(Word w, Path txtPath, Path txtPathNew, Path groupDir, boolean deletePrevious) throws IOException, ParseException {
        if (!Files.exists(txtPathNew)) {
            Files.copy(txtPath, txtPathNew);
            deletePreviousIfNeeded(deletePrevious, txtPath, txtPathNew);
        }
        Word wordAnalyzed = Analyzer.analyzeTxtFile(txtPathNew.getParent().toString(), txtPathNew.toFile());
        if (!Objects.equals(wordAnalyzed.getSource(), w.getSource())) {
            Files.copy(
                    txtPath,
                    Paths.get(groupDir + File.separator + w.getObjectName() + " - " + w.getSource() + ".txt")
            );
            deletePreviousIfNeeded(deletePrevious, txtPath, txtPathNew);
        }
    }

    /**
     * Deletes previous txt file if it is marked to do it.
     *
     * @param deletePrevious
     * @param pathOld
     * @param pathNew
     */
    private static void deletePreviousIfNeeded(boolean deletePrevious, Path pathOld, Path pathNew) throws IOException {
        if (deletePrevious && pathOld != pathNew) {
            Files.delete(pathOld);
            System.out.println(pathOld + " deleted");
        }
    }

    public static void copyNotFiles(Word w, Path pathOld, Path pathNew, Path groupDir, boolean deletePrevious) throws IOException {
        if (Files.exists(pathOld) && !Files.exists(pathNew)) {
            Files.copy(pathOld, pathNew);
            deletePreviousIfNeeded(deletePrevious, pathOld, pathNew);
        }
        if (Files.exists(pathOld) && Files.exists(pathNew)) {
            int dateSubtractPng = Files.getLastModifiedTime(pathOld).compareTo(Files.getLastModifiedTime(pathNew));
            if (dateSubtractPng != 0) {
                String name = pathOld.toFile().getName();
                String extension = name.substring(name.indexOf('.'));
                Files.copy(
                        pathOld,
                        Paths.get(groupDir + File.separator + w.getObjectName() + " - " + w.getSource() + extension)
                );
                deletePreviousIfNeeded(deletePrevious, pathOld, pathNew);
            }
        }
    }
}

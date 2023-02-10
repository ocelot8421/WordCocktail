package wordCocktail.fileModifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class WordCollector {

    public static void collectByDifficulty() throws IOException, ParseException { //TODO new words importing
        List<List<Word>> groups = Analyzer.analyzeAndReceiveDifficultyGroups();
        System.out.println("group size: " + groups.size());
        int n = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (!groups.get(i).isEmpty()){
                n = i;
                break;
            }
        }
        System.out.println("group(0) size: " + groups.get(n).size());
        File originalDir = groups.get(n).get(0).getFileTxt().getParentFile().getParentFile();
        Path levelsDir = Paths.get(originalDir + File.separator + "levels");

        if (!Files.exists(levelsDir)) {
            Files.createDirectory(levelsDir);
        }
        for (int i = 0; i < groups.size(); i++) {
            List<Word> groupI = groups.get(i);
            Path groupDir = Paths.get(levelsDir + File.separator + "level_" + i + " - db_" + groupI.size());
            if (!Files.exists(groupDir))
                Files.createDirectory(groupDir);
            for (Word w : groupI) {
                Path txtPath = w.getFileTxt().toPath();
                Path txtPathNew = Paths.get(groupDir + File.separator + w.getFileTxt().getName());
                copyTxt(w, txtPath, txtPathNew, groupDir);

                Path pngPath = w.getFilePng().toPath();
                Path pngPathNew = Paths.get(groupDir + File.separator + w.getFilePng().getName());
                copyNotFiles(w, pngPath, pngPathNew, groupDir);

                Path lnkPath = w.getFileLink().toPath();
                Path lnkPathNew = Paths.get(groupDir + File.separator + w.getFileLink().getName());
                copyNotFiles(w, lnkPath, lnkPathNew, groupDir);

            }
        }
    }

    private static void copyTxt(Word w, Path txtPath, Path txtPathNew, Path groupDir) throws IOException, ParseException {
        if (!Files.exists(txtPathNew))
            Files.copy(txtPath, txtPathNew);
        Word wordAnalyzed = Analyzer.analyzeTxtFile(txtPathNew.getParent().toString(), txtPathNew.toFile());//TODO PAth anf File usage is not consist
        if (!Objects.equals(wordAnalyzed.getSource(), w.getSource()))
            Files.copy(
                    txtPath,
                    Paths.get(groupDir + File.separator + w.getObjectName() + " - " + w.getSource() + ".txt")
            );
    }

    private static void copyNotFiles(Word w, Path pngPath, Path pngPathNew, Path groupDir) throws IOException {
        if (Files.exists(pngPath) && !Files.exists(pngPathNew))
            Files.copy(pngPath, pngPathNew);
        if (Files.exists(pngPathNew) && Files.exists(pngPathNew)) {
            int dateSubtractPng = Files.getLastModifiedTime(pngPath).compareTo(Files.getLastModifiedTime(pngPathNew));
            if (dateSubtractPng != 0)
                Files.copy(
                        pngPath,
                        Paths.get(groupDir + File.separator + w.getObjectName() + " - " + w.getSource() + ".png")
                );
        }
    }

    private static void makeGroupDirectory(List<Word> words) {
        System.out.println(words);
    }
}

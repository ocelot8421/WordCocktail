package wordCocktail.layer1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static wordCocktail.Main.ENG;
import static wordCocktail.Main.HUN;
import static wordCocktail.layer1.Questioner.promptForUTF8Answer;

/**
 * Handles directories these contain words collected by knowledge levels. Works in "levels" directory.
 */
public class Level {
    public static void exerciseByLevelOrDate(String groupsDirName) throws IOException, ParseException {
        String stringDir = promptForUTF8Answer("Where are " +
                groupsDirName + " directory? For example: C:/words/english/...)");
        isLevelsDir(stringDir, groupsDirName);
        Path pathPrompted = Paths.get(stringDir);
        File[] level_i_directories = Objects.requireNonNull(pathPrompted.toFile().listFiles());
        printContentSize(level_i_directories);
        int levelsNum = level_i_directories.length;
        String directory = chooseDirToExercise(stringDir, levelsNum);
        DifficultyCollector.collectByDifficulty(true, directory);
    }

    /**
     * Prints how many elements are in each of "level_x" directory.
     *
     * @param level_i_directories File array that is converted from user's string input.
     */
    private static void printContentSize(File[] level_i_directories) {
        String name;
        int size;
        int sizePng;
        int sizeLnk;
        for (int i = 0; i < level_i_directories.length; i++) {
            size = Objects.requireNonNull(level_i_directories[i].list()).length;
            name = level_i_directories[i].toPath().getFileName().toString();
            name = name.substring(0, name.indexOf("_") + 2);
            sizePng = (int) Arrays.stream(Objects.requireNonNull(level_i_directories[i].list()))
                    .filter(e -> e.endsWith(".png")).count();
            sizeLnk = (int) Arrays.stream(Objects.requireNonNull(level_i_directories[i].list()))
                    .filter(e -> e.endsWith(".lnk")).count();
            System.out.println(i + ": " + name + ": " + String.format("%3d", size) + "     " +
                    i + ": Eng: " + String.format("%3d", sizePng) + "     " +
                    i + ": Hun: " + String.format("%3d", sizeLnk));
        }
    }

    /**
     * Check the user input if it is the path of "levels" directory.
     *
     * @param stringDir     user's string input.
     * @param groupsDirName
     */
    private static void isLevelsDir(String stringDir, String groupsDirName) throws IOException, ParseException {
        Path pathPrompted = Paths.get(stringDir);
        if (!pathPrompted.getFileName().toString().equals(groupsDirName)) {
            System.out.println("\nPlease, type the path that contains \"" + groupsDirName + "\" directory");
            exerciseByLevelOrDate(groupsDirName);
        }
    }

    /**
     * Navigates the user to the "level_x" directory that is chosen.
     * Print words by chosen function (recognise english word or exercise translating from hungarian to english.
     *
     * @param stringDir directory-path asked from the user
     * @param levelsNum number of how many "level_x" directories in "levels" directory
     * @throws IOException
     */
    private static String chooseDirToExercise(String stringDir, int levelsNum) throws IOException {
        int chosenDir = Questioner.promptIntAnswer("Which directory do you want to exercise? 0 - " + (levelsNum - 1));
        if (chosenDir < 0 || chosenDir > levelsNum) {
            System.out.println("Choose between 0 and " + (levelsNum - 1));
            chooseDirToExercise(stringDir, levelsNum);
        }
        char flag = Questioner.promptCharAnswer("What do you want to do?\n" +
                "e: learning new English words\n" +
                "h: practice translating from Hungarian to english\n");
        while (!List.of('h', 'e').contains(flag)) {
            System.out.println(flag);
            flag = Questioner.promptCharAnswer("Waiting for valid answer... (e, h)");
        }
        String parentDirPath = stringDir + File.separator + "level_" + chosenDir;
        switch (flag) {
            case 'e':
                System.out.println("Starting English word recognition task...\n");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(parentDirPath, ENG);
                break;
            case 'h':
                System.out.println("Starting a translation task from Hungarian to English...\n");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(parentDirPath, HUN);
                break;
        }
        return parentDirPath;
    }
}

package wordCocktail;

import wordCocktail.layer1.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static final String HUN = ".lnk"; //if the names of links are hungarian words
    public static final String ENG = ".png"; //if the names of images are english words
    private static final char E = 'e'; //function chooser - exercise english words
    private static final char H = 'h'; //function chooser - exercise translating hungarian words to english
    private static final char W = 'w'; //function chooser - name new words and screenshots
    private static final char A = 'a'; //function chooser - analyzes the knowledge of word
    private static final char G = 'g'; //function chooser - regroup into directory by knowledge level
    private static final char L = 'l'; //function chooser - exercise by levels
    private static final char D = 'd'; //function chooser - planed exercise date
    private static final char T = 'm'; //function chooser - for test/debug with windows command line - not appears to the user

    public static void main(String[] args) throws IOException, ParseException {
        starterQuestion(); //Asks what the user want to do
        System.out.println("End of the program");
    }

    public static void starterQuestion() throws IOException, ParseException {
        String printedFunctions = receiveFunctions();
        char flag = Questioner.promptCharAnswer(printedFunctions);
        flag = isValidCharAnswer(flag);
        switch (flag) {
            case A:
                String directory = Questioner.promptForUTF8Answer(
                        "Analyzing the knowledge level of given words...\n" +
                                "Where are the words to analyzeDirectory?\n" +
                                "(For example: C:/words/english/...)");
                Analyzer.analyzeDirectory(directory);
                Main.starterQuestion();
                break;
            case G:
                String parentDirPath = Questioner.promptForUTF8Answer(
                        "Regroup words by difficulty level...\n" +
                                "Where are the words to regroup?\n" +
                                "(For example: C:/words/english/...)");
                DifficultyCollector.collectByDifficulty(false, parentDirPath);
                Main.starterQuestion();
                break;
            case W:
                System.out.println("Importing new screenshots...\n");
                NameGiver.renameScreenshots();
                Main.starterQuestion();
                break;
            case E:
                String dir = Questioner.promptForUTF8Answer(
                        "Starting English word recognition task...\n" +
                                "Where are the images of words to learn?\n" +
                                "(For example: C:/words/english/...)");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(dir, ENG);
                Main.starterQuestion();
                break;
            case H:
                parentDirPath = Questioner.promptForUTF8Answer(
                        "Starting a translation task from Hungarian to English...\n" +
                                "Where are the images of words to learn?\n" +
                                "(For example: C:/words/english/...)");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(parentDirPath, HUN);
                Main.starterQuestion();
                break;
            case L:
                System.out.println("Starting exercise by levels...\n");
                Level.exerciseByLevelOrDate("levels");
                Main.starterQuestion();
                break;
            case D:
                Questioner.promptForUTF8Answer(
                        "Looking for words are needed to exercise per given day...\n" +
                                "Where are \"dates\" directory?\n" +
                                "(For example: C:/words/english/...)");
                Level.exerciseByLevelOrDate("dates");
                Main.starterQuestion();
                break;
            case T:
                //not appears to the user
//                System.out.println("Starting test function...");
//                System.out.println("Test finished");
                break;
        }
    }

    private static char isValidCharAnswer(char flag) {
        while (!List.of(E, H, W, A, G, L, D, T).contains(flag)) {
            flag = Questioner.promptCharAnswer("Waiting for valid answer... (" + E + H + W + A + G + L + D + ")");
        }
        return flag;
    }

    private static String receiveFunctions() {
        return "What do you want to do?\n" +
                E + ": learning new English words\n" +
                H + ": practice translating from Hungarian to english\n" +
                W + ": rename screenshot of new words\n" +
                G + ": regroup into directory by knowledge level\n" +
                A + ": analyze the knowledge of word\n" +
                D + ": updates the next planned repeating date\n" +
                L + ": exercise by levels";
    }
}

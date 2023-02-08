package wordCocktail;

import wordCocktail.fileModifier.NameGiver;
import wordCocktail.questioner.QuestionProviderFromAllWords;
import wordCocktail.questioner.Questioner;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static final String HUN = ".lnk"; //if the names of links are hungarian words
    private static final String ENG = ".png"; //if the names of images are english words
    private static final char E = 'e'; //function chooser - exercise english words
    private static final char H = 'h'; //function chooser - exercise translating hungarian words to english
    private static final char W = 'w'; //function chooser - name new words and screenshots
    private static final char A = 'a'; //function chooser - analyzes the knowledge of word

    public static void main(String[] args) throws IOException, ParseException {
        starterQuestion(); //Asks what the user want to do
        System.out.println("End of the program");
    }

    //Asks the user what she/he wants to do at the beginning of the program
    private static void starterQuestion() throws IOException, ParseException {
        char flag = Questioner.promptCharAnswer("What do you want to do?\n" +
                E + ": learning new English words\n" +
                H + ": practice translating from Hungarian to english\n" +
                W + ": rename screenshot of new words\n" +
                A + ": analyzes the knowledge of word");
        while (!List.of(E, H, W, A).contains(flag)) {
            System.out.println(flag);
            flag = Questioner.promptCharAnswer("Waiting for valid answer... (" + E + H + W + A + ")");
        }
        switch (flag) {
            case A:
                System.out.println("Analyzing the knowledge level of given words -- BETA");
                Analyzer.analyze();
                break;
            case W:
                System.out.println("Importing new screenshots...\n");
                NameGiver.renameScreenshots();
                break;
            case E:
                System.out.println("Starting English word recognition task...\n");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(ENG);
                break;
            case H:
                System.out.println("Starting a translation task from Hungarian to English...\n");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(HUN);
                break;
        }
    }
}

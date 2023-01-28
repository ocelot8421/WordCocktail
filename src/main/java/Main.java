import betaVersion.QuestionProviderFromAllWords;
import fileModifier.NameGiver;
import fileModifier.Trainer;
import questioner.Questioner;

import java.util.List;

public class Main {
    private static final String HUN = ".lnk"; //if the names of links are hungarian words
    private static final String ENG = ".png"; //if the names of images are english words
    private static final char E = 'e'; //function chooser - exercise english words
    private static final char H = 'h'; //function chooser - exercise translating hungarian words to english
    private static final char W = 'w'; //function chooser - name new words and screenshots
    private static final char X = 'x'; //function chooser - beta - exercise with every saved english words
    private static final char Y = 'y'; //function chooser - beta - exercise translating every saved hungarian words to english

    public static void main(String[] args) {
        starterQuestion(); //Asks what the user want to do
        System.out.println("End of the program");
    }

    //Asks the user what she/he wants to do at the beginning of the program
    private static void starterQuestion() {
        char flag = Questioner.promptCharAnswer("What do you want to do?\n" +
                E + ": learning new English words\n" +
                H + ": practice translating from Hungarian to english\n" +
                W + ": rename screenshot of new words\n" +
                X + ": new function - handle the english words separately\n" +
                Y + ": new function - handle the hungarian words separately");
        while (!List.of(E, H, W, X, Y).contains(flag)) {
            System.out.println(flag);
            flag = Questioner.promptCharAnswer("Waiting for valid answer... (" + E + H + W + X + Y + ")");
        }
        switch (flag) {
            case E:
                System.out.println("Starting English word recognition task...\n");
                Trainer.askRandomWord(ENG); //Continually asking for random words from the list until the list is empty
                break;
            case H:
                System.out.println("Starting a translation task from Hungarian to English...\n");
                Trainer.askRandomWord(HUN);
                break;
            case W:
                System.out.println("Importing new screenshots...\n");
                NameGiver.renameScreenshots();
                break;
            case X:
                System.out.println("Beta version - handle english words separately\n");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(ENG);
                break;
            case Y:
                System.out.println("Beta version - handle hungarian words separately\n");
                QuestionProviderFromAllWords.askRandomlyFromAllWords(HUN);
                break;
        }
    }
}

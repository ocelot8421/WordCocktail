import static fileModifier.Trainer.askRandomWord;
import static fileModifier.Trainer.importNewScreenshots;
import static questioner.Questioner.promptCharAnswer;

public class Main {

    private static final String HUN = ".lnk"; //if the links' names are hungarian words
    private static final String ENG = ".png"; //if the images' names are english words

    public static void main(String[] args) {
        starterQuestion(); //Asks what the user want to do
        System.out.println("End of the program");
    }

    //Asks the user what she/he wants to do at the beginning of the program
    private static void starterQuestion() {
        char flag = promptCharAnswer("What do you want to do?\n" +
                "e: learning new English words\n" +
                "h: practice translating from Hungarian to english\n" +
                "n: rename screenshot of new words");
        while (flag != 'e' && flag != 'h' && flag != 'n') {
            System.out.println(flag);
            flag = promptCharAnswer("Waiting for valid answer... (e, h, n)");
        }
        switch (flag) {
            case 'e':
                System.out.println("Starting English word recognition task...\n");
                askRandomWord(ENG); //Continually asking for random words from the list until the list is empty
                break;
            case 'h':
                System.out.println("Starting a translation task from Hungarian to English...\n");
                askRandomWord(HUN); //TODO https://www.baeldung.com/java-string-encode-utf-8#standards-charset
                break;
            case 'n':
                System.out.println("Importing new screenshots...\n");
                importNewScreenshots();
                break;
        }
    }
}

import static fileModifier.Trainer.askRandomEnglishWord;
import static fileModifier.Trainer.importNewScreenshots;
import static questioner.Questioner.promptCharAnswer;

public class Main {

    public static void main(String[] args) {

        //Asking what the user want to do
        starterQuestion();

        System.out.println("End of the program");
    }

    //Asking the user what it want to do at the beginning of the program
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
                //Continually asking for random words from the list until the list is empty
                askRandomEnglishWord();
                break;
            case 'h':
                System.out.println("Starting a translation task from Hungarian to English...\n"); //TODO
                break;
            case 'n':
                System.out.println("Importing new screenshots...\n");
                importNewScreenshots();
                break;
        }
    }


}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static questioner.Questioner.*;

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
            flag = promptCharAnswer("Waiting for valid answer... (e, h, n)");
        }
        switch (flag) {
            case 'e':
                System.out.println("Starting English word recognition task...\n");
                //Continually asking for random words from the list until the list is empty
                engine();
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

    //Import screenshots about new words
    private static void importNewScreenshots() {
        NameGiver nameGiver = new NameGiver();
        nameGiver.renameScreenshots();
    }

    //Learning new English words
    private static void engine() {
        //Directory that contains the images of words
        String parentDirPath = askDirectoryLocation("Where are the images of words to learn?\n" +
                "(For example: C:/words/english/...)");
        System.out.println("Searching words...\n");
        //List of english words
        List<String> englishWords = listEnglishWords(parentDirPath);

        //Continually asking for random words from the list until the list is empty
        Random random = new Random();
        char wantContinue = 'y';
        while (wantContinue == 'y') {
            if (englishWords.size() > 0) {
                String engWord = showRandomWord(random, englishWords);
                askRemovingEngWord(englishWords, engWord);
                JFrame jFrame = askShowingImage(parentDirPath, engWord);
                wantContinue = promptCharAnswer("Do you want continue? (y/n)");
                jFrame.setVisible(false);
            } else {
                System.out.println("The list of words to learn is empty. You've finished the task. :)");
                wantContinue = 'n';
            }
        }
    }

    //Asking if you would like to see the picture(screenshot from a video) of the english word.
    //Tutorial of image-handling: https://www.baeldung.com/java-images).
    private static JFrame askShowingImage(String parentDirPath, String engWord) {
        char show = promptCharAnswer("Show picture? (y/n)");
        JFrame jFrame = new JFrame();
        if (show == 'y') {
            String imagePath = parentDirPath + File.separator + engWord + ".png";
            BufferedImage myPicture;
            try {
                myPicture = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Graphics2D g = (Graphics2D) myPicture.getGraphics();
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.BLUE);
            g.drawRect(10, 10, myPicture.getWidth() - 20, myPicture.getHeight() - 20);

            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            JPanel jPanel = new JPanel();
            jPanel.add(picLabel);

            jFrame.setSize(new Dimension(myPicture.getWidth(), myPicture.getHeight()));
            jFrame.add(jPanel);
            jFrame.setVisible(true);
        }
        return jFrame;
    }

    //Asking if you would like to remove the word from the list that contains words to learn.
    private static void askRemovingEngWord(List<String> englishWords, String engWord) {
        char remove = promptCharAnswer("Remove word? (y/n)");
        if (remove == 'y') {
            englishWords.remove(engWord);
            System.out.println("Remaining words: " + englishWords.size());
        }
    }

    //Showing a random word from the list of words to learn.
    private static String showRandomWord(Random random, List<String> englishWords) {
        int randomInt = random.nextInt(englishWords.size());
        String engWord = englishWords.get(randomInt);
        System.out.println("Your word:  " + engWord);
        return engWord;
    }

    //Making a list of the pgn-files' name.
    //Doesn't handle pgn that's name starts "Ké" (origins from Képernyőkép(hu) = Screenshot(eng)).
    private static List<String> listEnglishWords(String parentDirPath) {
        File parentDir = new File(parentDirPath);
        List<String> englishWords = new ArrayList<>();
        for (String word : Objects.requireNonNull(parentDir.list())) {
            String[] tempWords = word.split("\\.");
            if (tempWords.length > 1 && Objects.equals(tempWords[1], "png")
                    && !tempWords[0].startsWith("Ké")) {
                englishWords.add(tempWords[0]);
            }
        }
        return englishWords;
    }
}

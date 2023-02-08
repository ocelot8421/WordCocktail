package wordCocktail.questioner;

import wordCocktail.txtModifiers.TxtWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import static wordCocktail.fileModifier.LinkCreator.openLink;
import static wordCocktail.questioner.Questioner.*;

public class QuestionProviderFromAllWords {

    public static void askRandomlyFromAllWords(String language) throws IOException {
        //The directory that contains the images of words
        String parentDirPath = askForUTF8Answer("Where are the images of words to learn?\n" +
                "(For example: C:/words/english/...)");
        System.out.println("Searching words...\n");

        //List of words that are needed to learn
        List<String> words = listWords(parentDirPath, language);


        //Continually asks for random words from the list until the list is empty
        Random random = new Random();
        char wantContinue = 'y';
        String word;
        while (wantContinue == 'y') {
            if (words.size() > 0) {
                word = showRandomWord(random, words);
                askRemovingEngWord(words, word);
                TxtWriter.saveTrainingDate(parentDirPath, word);
                if (Objects.equals(language, ".png")) {
                    JFrame jFrame = showImage(parentDirPath, word, language);
                    wantContinue = promptCharAnswer("Do you want continue? (y/n)");
                    jFrame.setVisible(false);
                } else if (Objects.equals(language, ".lnk")) {
                    openLink(parentDirPath, word, language);
                    wantContinue = promptCharAnswer("Do you want continue? (y/n)");
                }
            } else {
                System.out.println("The list of words to learn is empty. You've finished the task. :)");
                wantContinue = 'n';
            }
        }
    }

    //Asks if you would like to see the picture(screenshot from a video) of the english word.
    //Tutorial of image-handling: https://www.baeldung.com/java-images).
    private static JFrame showImage(String parentDirPath, String word, String language) {
        char show = promptCharAnswer("Show picture? (y/n)");
        JFrame jFrame = new JFrame();
        if (show == 'y') {
            String imagePath = parentDirPath + File.separator + word + language;
            try {
                BufferedImage myPicture = ImageIO.read(new File(imagePath));
                Graphics2D g = (Graphics2D) myPicture.getGraphics();
                g.setStroke(new BasicStroke(3));
                g.setColor(Color.BLUE);
                g.drawRect(10, 10, myPicture.getWidth() - 20, myPicture.getHeight() - 20); //TODO too large
                JLabel picLabel = new JLabel(new ImageIcon(myPicture));
                JPanel jPanel = new JPanel();
                jPanel.add(picLabel);
                jFrame.setSize(new Dimension(myPicture.getWidth(), myPicture.getHeight()));
                jFrame.add(jPanel);
                jFrame.setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jFrame;
    }

    //Asks if you would like to remove the word from the list that contains words to learn.
    private static void askRemovingEngWord(List<String> englishWords, String engWord) {
        char remove = promptCharAnswer("Remove word? (y/n)");
        if (remove == 'y') {
            englishWords.remove(engWord);
            System.out.println("Remaining words: " + englishWords.size());
        }
    }

    //Shows a random word from the list of words to learn.
    private static String showRandomWord(Random random, List<String> words) {
        int randomInt = random.nextInt(words.size());
        String word = words.get(randomInt);
        System.out.println("Your word:  " + word);
        return word;
    }

    //Makes a list of the pgn-files' name.
    //Doesn't handle pgn that's name starts "Ké" (origins from Képernyőkép(hu) = Screenshot(eng)).
    private static List<String> listWords(String parentDirPath, String language) {
        File parentDir = new File(parentDirPath);
        String[] extension = language.split("\\.");
        String linkOrPng = extension[extension.length - 1];
        List<String> englishWords = new ArrayList<>();
        for (String word : Objects.requireNonNull(parentDir.list())) {
            byte[] bytes = word.getBytes(StandardCharsets.UTF_8); //https://northcoder.com/post/java-console-output-with-utf-8/
            String wordUtf8 = new String(bytes, StandardCharsets.UTF_8);
            String[] tempWords = wordUtf8.split("\\.");
            if (tempWords.length > 1 && Objects.equals(tempWords[1], linkOrPng)
                    && !tempWords[0].startsWith("Ké")) {
                englishWords.add(tempWords[0]);
            }
        }
        return englishWords;
    }
}

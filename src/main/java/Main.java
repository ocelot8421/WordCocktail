import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Main {

    public static void main(String[] args){
        Random random = new Random();

        //Directory that contains the images of words
        System.out.println("Where are the images of words? \n(For example: C:/words/english/...)");
        Scanner scanner = new Scanner(System.in);
        String parentDirPath = makeParentDirPAth(scanner);

        //List of english words
        List<String> englishWords = listEnglishWords(parentDirPath);

        //Engine
        char wantContinue = 'y';
        while (wantContinue == 'y') {
            if (englishWords.size() > 0) {
                String engWord = randomEnglishWord(random, englishWords);
                askRemovingEngWord(scanner, englishWords, engWord);
//                ImagePlus imp = askShowingEngWordImage(scanner, parentDirPath, engWord);
                JFrame pic = askShowingImage(scanner, parentDirPath, engWord);
                wantContinue = promptAnswer(scanner, "Do you want continue? (y/n)");
//                imp.close();
                pic.setVisible(false);
            } else {
                System.out.println("The end. You've finished the task. :)");
                wantContinue = 'n';
            }
        }
        System.out.println("End of the program");
    }

    private static JFrame askShowingImage(Scanner scanner, String parentDirPath, String engWord) {
        char show = promptAnswer(scanner, "Show picture? (y/n)");
        JFrame f = new JFrame();
        if (show == 'y') {
            //Open image (tutorial of image-handling by AWT: https://www.baeldung.com/java-images#imagej)
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

            f.setSize(new Dimension(myPicture.getWidth(), myPicture.getHeight()));
            f.add(jPanel);
            f.setVisible(true);
        }
        return f;
    }

    private static String makeParentDirPAth(Scanner scanner) {
        String parentDirPath = scanner.nextLine();
        StringBuilder parentDirPathOSIndependent = new StringBuilder();
        for (char s : parentDirPath.toCharArray()) {
            parentDirPathOSIndependent.append(s);
        }
        return parentDirPathOSIndependent.toString();
    }

    private static void askRemovingEngWord(Scanner scanner, List<String> englishWords, String engWord) {
        char remove = promptAnswer(scanner, "Remove word? (y/n)");
        if (remove == 'y') {
            englishWords.remove(engWord);
            System.out.println("Remaining words: " + englishWords.size());
        }
    }

    private static char promptAnswer(Scanner scanner, String question) {
        System.out.println(question);
        return scanner.next().charAt(0);
    }

    //Random english word
    private static String randomEnglishWord(Random random, List<String> englishWords) {
        int randomInt = random.nextInt(englishWords.size());
        String engWord = englishWords.get(randomInt);
        System.out.print("Your word:  ");
        System.out.println(engWord);
        return engWord;
    }

    //List of english words
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

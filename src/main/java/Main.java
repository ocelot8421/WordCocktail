import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Main {
    //colors for highlighting the word
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        Random random = new Random();

        //Directory that contains the images of words
        System.out.println("Where are the images of words? \n(For example: C:/words/english/...)");
        Scanner scanner = new Scanner(System.in);
        String parentDirPath = scanner.nextLine();

        //List of english words
        List<String> englishWords = listEnglishWords(parentDirPath);

        //Engine
        char wantContinue = 'y';
        while (wantContinue == 'y') {
            if (englishWords.size() > 0) {
                String engWord = randomEnglishWord(random, englishWords);
                askRemovingEngWord(scanner, englishWords, engWord);
                ImagePlus imp = askShowingEngWordImage(scanner, parentDirPath, engWord);
                wantContinue = promptAnswer(scanner, "Do you want continue? (y/n)");
                imp.close();
            } else {
                System.out.println("The end. You've finished the task. :)");
                wantContinue = 'n';
            }
        }
    }

    private static ImagePlus askShowingEngWordImage(Scanner scanner, String parentDirPath, String engWord) {
        char show = promptAnswer(scanner, "Show picture? (y/n)");
        ImagePlus imp = new ImagePlus();
        if (show == 'y') {
            //Open image (tutorial of image-handling by ImageJ: https://www.baeldung.com/java-images#imagej)
            String imagePath = parentDirPath + File.separator + engWord + ".png";
            imp = openImage(imagePath);
            return imp;
        }
        return imp;
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
        System.out.print("Your word:  " + ANSI_CYAN);
        System.out.println(engWord + ANSI_RESET);
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

    private static ImagePlus openImage(String imagePath) {
        ImagePlus imp = IJ.openImage(imagePath);

        ImageProcessor ip = imp.getProcessor();
        ip.setColor(Color.BLUE);
        ip.setLineWidth(4);
        ip.drawRect(10, 10, imp.getWidth() - 20, imp.getHeight() - 20);
        imp.show();
        return imp;
    }

}

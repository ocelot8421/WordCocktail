import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //Directory that contains the images of words
        System.out.println("Where are the images of words? (For example: C:/words/english)");
        Scanner scanner = new Scanner(System.in);
        String parentDirPath = scanner.nextLine();

        //List of english words
        List<String> englishWords = listEnglishWords(parentDirPath);

        //Engine
        char wantContinue = 'y';
        while (wantContinue == 'y') {
            String engWord = randomEnglishWord(englishWords);
            askRemovingEngWord(scanner, englishWords, engWord);
            askShowingEngWordImage(scanner, parentDirPath, engWord);
            wantContinue = promptAnswer(scanner, "Do you want continue? (y/n)");
        }
    }

    private static void askShowingEngWordImage(Scanner scanner, String parentDirPath, String engWord) {
        char show = promptAnswer(scanner, "Show picture? (y/n)");
        if (show == 'y') {
            //Open image (tutorial of image-handling by ImageJ: https://www.baeldung.com/java-images#imagej)
            String imagePath = parentDirPath + File .separator + engWord + ".png";
            openImage(imagePath);
        }
    }

    private static void askRemovingEngWord(Scanner scanner, List<String> englishWords, String engWord) {
        char remove = promptAnswer(scanner, "Remove word? (y/n)");
        if (remove == 'y') {
            englishWords.remove(engWord);
        }
    }

    private static char promptAnswer(Scanner scanner, String question) {
        System.out.println(question);
        char answer = scanner.next().charAt(0);
        return answer;
    }

    //Random english word
    private static String randomEnglishWord(List<String> englishWords) {
        Random random = new Random();
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
            if (Objects.equals(tempWords[1], "png")) {
                englishWords.add(tempWords[0]);
            }
        }
        return englishWords;
    }

    private static void openImage(String imagePath) {
        ImagePlus imp = IJ.openImage(imagePath);

        ImageProcessor ip = imp.getProcessor();
        ip.setColor(Color.BLUE);
        ip.setLineWidth(4);
        ip.drawRect(10, 10, imp.getWidth() - 20, imp.getHeight() - 20);

        imp.show();
    }

}

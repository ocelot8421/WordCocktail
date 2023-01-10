import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //List of english words
        String parentDirPath = "D:\\Hajni\\1. Aktuális\\Angol\\HarwardX CS50 képernyőképek\\David J Malan recitations";
        File parentDir = new File(parentDirPath);
        List<String> englishWords = new ArrayList<>();
        for (String word : Objects.requireNonNull(parentDir.list())) {
            String[] tempWords = word.split("\\.");
            if (Objects.equals(tempWords[1], "png")){
                englishWords.add(tempWords[0]);
            }
        }

        //Random english word
        Random random = new Random();
        int randomInt = random.nextInt(englishWords.size());
        String engWord = englishWords.get(randomInt);
        System.out.println(engWord);

        //Open image (tutorial of image-handling by ImageJ: https://www.baeldung.com/java-images#imagej)
        String imagePath = parentDirPath + File .separator + engWord + ".png";
        openImage(imagePath);
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

package wordCocktail.layer2;

import java.io.File;

public class FileChanger {
    /**
     * Changes temp file to the original. In practice, updates original file.
     * @param temp temporal file that contains new data
     * @param original old file contains data before update
     * @param feedBack printed string to inform the user
     */
    public static void changeTempToOriginal(File temp, File original, String feedBack) {
        String pathOriginal = original.getPath();
        boolean delFlag = original.delete();
        boolean renameFlag = temp.renameTo(new File(pathOriginal));
        if (delFlag && renameFlag) {
            System.out.println(feedBack);
        }
    }
}

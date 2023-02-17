package wordCocktail.layer2;

import wordCocktail.layer1.Questioner;
import wordCocktail.thridPartClasses.ShortcutFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Creates a link to the png file.
 */
public class LinkCreator {

    /**
     * Creates a shortcut for a renamed screenshot
     *
     * @param source   path of screenshot
     * @param linkPath path of link
     */
    public static void createLink(String source, String linkPath) {
        try {
            ShortcutFactory.createShortcut(source, linkPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens the link.
     *
     * @param parentDirPath directory where the links are
     * @param word          Hungarian part of word-pair
     * @param language      HUN = ".lnk" (ENG = ".png")
     * @source <a href="https://stackoverflow.com/questions/13145942/creating-a-shortcut-file-from-java">stackoverflow.com, Jackson Brienen, ShortcutFactory</a>
     */
    public static void openLink(String parentDirPath, String word, String language) {
        char show = Questioner.promptCharAnswer("Show picture? (y/n)");
        if (show == 'y') {
            String linkPath = parentDirPath + File.separator + word + language;
            String vbsCode = String.format(
                    "Set wsObj = WScript.CreateObject(\"WScript.shell\")%n" +
                            "wsObj.Run \"\"\"" + linkPath + "\"\"\"");
            try {
                ShortcutFactory.newVBS(vbsCode);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

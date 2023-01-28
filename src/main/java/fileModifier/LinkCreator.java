package fileModifier;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static questioner.Questioner.promptCharAnswer;
import static thridPartClasses.ShortcutFactory.createShortcut;
import static thridPartClasses.ShortcutFactory.newVBS;

public class LinkCreator {

    //Create a shortcut for a renamed screenshot
    public static void createLink(String source, String linkPath) {
        try {
            createShortcut(source, linkPath); //TODO work in only IDE not in terminal
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Open shortcut
    public static void openLink(String parentDirPath, String word, String language) {
        char show = promptCharAnswer("Show picture? (y/n)");
        if (show == 'y') {
            String linkPath = parentDirPath + File.separator + word + language;
            String vbsCode = String.format(
                    "Set wsObj = WScript.CreateObject(\"WScript.shell\")%n" +
                            "wsObj.Run \"\"\"" + linkPath + "\"\"\"");
            try {
                newVBS(vbsCode);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

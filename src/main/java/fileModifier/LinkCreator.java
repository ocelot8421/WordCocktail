package fileModifier;

import java.io.FileNotFoundException;

import static thridPartClasses.ShortcutFactory.createShortcut;

public class LinkCreator {

    //Create a link for a renamed screenshot
    public static void createLink(String source, String linkPath) {
        try {
            createShortcut(source,linkPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Rename the link for the hungarian translated word
}

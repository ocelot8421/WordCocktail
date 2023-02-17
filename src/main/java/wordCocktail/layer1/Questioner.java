package wordCocktail.layer1;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Tool to get information from user. Prompts char, int answer, or for that is encoded in UTF-8.
 */
public class Questioner {
    static Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    /**
     * Prompt for UTF-8 encoded answer. Uses the Console class. (Windows command line console.)
     * @source <a href="https://stackoverflow.com/questions/54118307/how-can-i-get-user-input-in-utf-8">stackoverflow.com, user1257</a>
     * @param question Question printed to the user to get the answer.
     * @return Returns a string converted from the user input (UTF-8).
     */
    public static String promptForUTF8Answer(String question) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console");
            System.exit(1);
        }
        System.out.println(question);
        return console.readLine("Your input: %n");
    }

    /**
     * Prompts char.
     * @param question Question printed to the user to get the answer.
     * @return Returns char, if any, otherwise whitespace.
     */
    public static char promptCharAnswer(String question) {
        System.out.println(question);
        String answer = scanner.nextLine();
        if (answer.length() == 1) {
            return answer.charAt(0);
        } else {
            return ' ';
        }
    }

    /**
     * Prompts int.
     * @param question Question printed to the user to get the answer.
     * @return Returns int.
     */
    public static int promptIntAnswer(String question) {
        System.out.println(question);
        return scanner.nextInt();
    }
}

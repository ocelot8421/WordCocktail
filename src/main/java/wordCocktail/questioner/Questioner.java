package wordCocktail.questioner;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//Tool to get information from user
public class Questioner {
    static Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    //Asking for location of directory
    public static String askDirectoryLocation(String question) {
        System.out.println(question);
//        String answerRaw = scanner.nextLine();
        byte[] answerNotUtf8 = scanner.nextLine().getBytes(StandardCharsets.UTF_8);
        String answer = new String(answerNotUtf8, StandardCharsets.UTF_8);
        System.out.println();
        return answer;
    }

    public static String askForUTF8Answer(String question) {
        //https://stackoverflow.com/questions/54118307/how-can-i-get-user-input-in-utf-8
        Console console = System.console();
        if (console == null) {
            System.err.println("No console");
            System.exit(1);
        }
        System.out.println(question);
        return console.readLine("type input: %n");
    }

    //Asking a yes-no question.
    public static char promptCharAnswer(String question) {
        System.out.println(question);
        String answer = scanner.nextLine();
        if (answer.length() == 1) {
            return answer.charAt(0);
        } else {
            return ' ';
        }
    }
}

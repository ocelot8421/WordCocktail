package questioner;

import java.util.Scanner;

//Tool to get information from user
public class Questioner {
    static Scanner scanner = new Scanner(System.in);

    //Asking for location of directory
    public static String askDirectoryLocation(String question) {
        System.out.println(question);
        String answer = scanner.nextLine();
        System.out.println();
        return answer;
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

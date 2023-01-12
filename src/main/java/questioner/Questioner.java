package questioner;

import java.util.Scanner;

//Tool to get information from user
public class Questioner {
    static Scanner scanner = new Scanner(System.in);

    //Asking for location of directory
    public static String askDirectoryLocation(String question) {
        System.out.println(question);
        scanner.nextLine();
        String answer = scanner.nextLine();
        System.out.println("Searching words...\n");
        return answer;
    }

    //Asking a yes-no question.
    public static char promptCharAnswer(String question) {
        System.out.println(question);
        return scanner.next().charAt(0);
    }
}

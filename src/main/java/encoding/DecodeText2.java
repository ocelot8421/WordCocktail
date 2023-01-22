package encoding;

import java.io.PrintStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DecodeText2 {
    public static void printHun() {
        PrintStream out = new PrintStream(System.out, true, UTF_8); // true = autoflush
        out.println("读写汉字");
        out.println("árvíztűrő tükörfúrógép");
    }
}

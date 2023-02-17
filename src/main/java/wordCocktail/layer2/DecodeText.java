package wordCocktail.layer2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

//
public class DecodeText {
    /**
     * Uses to prevent decoding issues.
     * @param input string that is needed to be encoded into another.
     * @param encoding name of encoding (usually "UTF-8")
     * @return Returns encoded string (usually used "UTF-8")
     * @throws IOException
     * @Example String word = DecodeText.decodeText(someString, "UTF-8");
     * @Source <a href="https://www.baeldung.com/java-char-encoding#importance_of_character_encoding">baeldung, encoding</a>
     */
    public static String decodeText(String input, String encoding) throws IOException {
        return
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(input.getBytes()),
                                Charset.forName(encoding)))
                        .readLine();
    }
}

package betaVersion;

import java.util.Date;

import static questioner.Questioner.askDirectoryLocation;

public class QuestionProviderFromAllWords {
    public static void askRandomlyFromAllWords(String language) {
        String parentDirPath = askDirectoryLocation("Where are the images of words to learn?\n" +
                "(For example: C:/words/english/...)");
        String wordEng = "subtract";
        //TODO askRandomlyFromAllWords
        //Make a list of actual words those are needed to repeat
        //Check the date when the word was saved as learnt last time
        Word word = new Word(parentDirPath, wordEng);
        Date lastDate = word.getLastDate();
        System.out.println(lastDate);
        //If the word dated to learn add to the list
        //Ask from the list randomly
        //Update its learning date
        //If the word has been saved as learnt the date will raise with the next period (1 day, 2 days, 4 days, 1 week, 2 weeks, so on)
        //Else the word is needed to learn tomorrow and the date will raise with only 1 day
    }
}

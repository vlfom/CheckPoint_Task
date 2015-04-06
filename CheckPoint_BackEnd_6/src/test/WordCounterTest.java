package test;

import main.WordCounter;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordCounterTest {
    @Test
    public void testCalc(){
        String fileName = "shakespeare.txt" ;
        long ms = System.currentTimeMillis();
        WordCounter.calculateFrequency(System.getProperty("user.dir") + "\\res\\" + fileName, 20);
        WordCounter.calculateFrequency(System.getProperty("user.dir") + "\\res\\" + fileName, 40);
        WordCounter.calculateFrequency(System.getProperty("user.dir") + "\\res\\" + fileName, 60);
        ms = System.currentTimeMillis() - ms;
        assertTrue(ms < 60000);
    }
}

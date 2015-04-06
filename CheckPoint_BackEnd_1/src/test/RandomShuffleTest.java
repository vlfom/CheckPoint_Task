package test;

import org.junit.Test;
import main.RandomShuffle;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RandomShuffleTest {
    @Test
    public void shuffleTest() {
        for( int SIZE = 8 ; SIZE < 80000 ; SIZE += 800 ) { // size of array
            int[] testArray = new int[SIZE] ;
            int lastElement = 0 ;
            for(int i = 0 ; i < SIZE ; ++i ) {
                // generating new array element as last element + random(4) + 1
                lastElement = lastElement +
                        (int) Math.round( Math.random() * 4 ) + 1 ;
                testArray[i] = lastElement ;
            }
            int[] arrayCopy = Arrays.copyOf(testArray, SIZE);
            int shuffledCount = 0;
            RandomShuffle.randomShuffle(testArray);
            for (int i = 0; i < SIZE; ++i)
                if (testArray[i] != arrayCopy[i])
                    ++shuffledCount;
            assertEquals(shuffledCount, SIZE / 4);
        }
    }
}
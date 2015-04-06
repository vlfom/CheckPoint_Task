package test;

import main.AsynchronousCalculator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsynchronousCalculatorTest {

    @Test
    public void testActionByThreadPool(){
        assertTrue(AsynchronousCalculator.calculateByThreadPool(1, 10));
        assertTrue(AsynchronousCalculator.calculateByThreadPool(3, 10));
        assertTrue(AsynchronousCalculator.calculateByThreadPool(6, 1000));
        assertTrue(AsynchronousCalculator.calculateByThreadPool(10, 10));
        assertTrue(AsynchronousCalculator.calculateByThreadPool(100, 100));
        assertTrue(AsynchronousCalculator.calculateByThreadPool(1000, 1000));
    }

    @Test
    public void testActionByExecutors(){
        assertTrue(AsynchronousCalculator.calculateByExecutors(1, 10));
        assertTrue(AsynchronousCalculator.calculateByExecutors(3, 10));
        assertTrue(AsynchronousCalculator.calculateByExecutors(6, 1000));
        assertTrue(AsynchronousCalculator.calculateByExecutors(10, 10));
        assertTrue(AsynchronousCalculator.calculateByExecutors(100, 100));
        assertTrue(AsynchronousCalculator.calculateByExecutors(1000, 1000));
    }

}

package arraylist.test;

import arraylist.main.MyArrayList;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.* ;

public class MyArrayListTest {
    @Test
    public void DateTimeTest() {
        LocalDateTime startTime = LocalDateTime.now() ;
        MyArrayList<Integer> myList = new MyArrayList<Integer>();
        myList.add(0) ;
        try {
            Thread.sleep(1500);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        myList.add(1) ;
        myList.removeBeforeDate(startTime.plusSeconds(1)) ;
        assertEquals((int)myList.get(0),1) ;
    }

    @Test
    public void UnitTest() {
        MyArrayList<Integer> myList ;
        boolean exception ;

        exception = false ;
        try {
            myList = new MyArrayList<Integer>(100);
        }
        catch (Exception e) {
            exception = true ;
        }
        assertFalse(exception) ;

        exception = false ;
        try {
            myList = new MyArrayList<Integer>(-100);
        }
        catch (Exception e) {
            exception = true ;
        }
        assertTrue(exception) ;

        myList = new MyArrayList<Integer>() ;

        assertTrue(myList.isEmpty()) ;
        assertEquals(myList.size(),0);

        myList.add(1) ;
        myList.clear() ;
        assertTrue(myList.isEmpty()) ;

        myList.add(0) ;
        myList.add(0) ;
        myList.add(1) ;
        myList.add(5) ;

        assertEquals(myList.toArray()[3], 5) ;
        assertEquals(myList.indexOf(0), 0) ;
        assertEquals(myList.lastIndexOf(0),1);

        myList.set(3, 6) ;
        assertEquals(myList.toArray()[3], 6) ;

        myList.add(2,10) ;
        assertEquals(myList.toArray()[2], 10) ;

        myList.remove(new Integer(10)) ;
        assertFalse(myList.contains(10)) ;

        myList.remove(0);
        assertEquals(myList.lastIndexOf(0),0);

        myList.remove(0) ;
        assertFalse(myList.contains(0)) ;
    }
}
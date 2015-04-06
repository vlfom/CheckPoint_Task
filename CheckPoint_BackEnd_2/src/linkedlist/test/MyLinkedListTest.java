package linkedlist.test;

import map.main.MyMap;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.* ;

public class MyLinkedListTest {
    @Test
    public void DateTimeTest() {
        LocalDateTime startTime = LocalDateTime.now() ;
        MyMap myMap = new MyMap() ;
        myMap.put("Java", "Good") ;
        try {
            Thread.sleep(1500);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        myMap.put("Python", "Simple") ;
        myMap.removeBeforeDate(startTime.plusSeconds(1)) ;
        assertFalse(myMap.containsKey("Java"));
    }

    @Test
    public void UnitTest() {
        MyMap myMap = new MyMap() ;

        assertTrue(myMap.isEmpty()) ;
        assertEquals(myMap.size(),0) ;

        myMap.put(1,"a") ;
        assertEquals(myMap.get(1), "a") ;
        assertTrue(myMap.containsKey(1)) ;
        assertTrue(myMap.containsValue("a")) ;

        myMap.remove(1) ;
        assertTrue(myMap.isEmpty()) ;
        assertFalse(myMap.containsKey(1)) ;

        myMap.put(2,"b") ;
        myMap.clear() ;
        assertTrue(myMap.isEmpty()) ;
        assertFalse(myMap.containsKey(2)) ;

        MyMap tempMap = new MyMap() ;
        tempMap.put(1,"a") ;
        tempMap.put(2,"b") ;

        myMap.put(3,"c") ;
        myMap.put(4,"d") ;
        myMap.putAll(tempMap) ;
        assertTrue(myMap.containsKey(1)) ;
        assertEquals(myMap.get(1), "a") ;
        assertTrue(myMap.containsKey(2)) ;
        assertEquals(myMap.get(2), "b") ;

        assertTrue(myMap.keySet().contains(1)) ;
        assertTrue(myMap.values().contains("a")) ;

        myMap = null ;
        tempMap = null ;
    }
}
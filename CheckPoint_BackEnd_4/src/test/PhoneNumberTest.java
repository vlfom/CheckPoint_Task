package test;

import main.PhoneNumber;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneNumberTest {

    private String goodNumbers[];
    private String badNumbers[];

    @Before
    public void setUp(){
        goodNumbers = new String[] {
                "999 25 34",
                "+3 (8066) 479-218-99",
                "(6342) 322-228-245",
                "7 (7832) 55-234-554",
                "+2 (0000) 234-234-234",
        };
        badNumbers = new String[] {
                "567-78 98",
                "367 783-34",
                "(673 786-78-32",
                "673) 786-78-32",
                "9 (7832) 234-234-234",
        };
    }

    @After
    public void tearDown(){
        goodNumbers = null;
        badNumbers = null;
    }

    @Test
    public void testCheckNumber(){
        for(String number : goodNumbers)
            assertEquals(PhoneNumber.isGood(number), true);
        for(String number : badNumbers)
            assertEquals(PhoneNumber.isGood(number), false);
    }
}

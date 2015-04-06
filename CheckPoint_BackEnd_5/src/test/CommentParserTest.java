package test;

import main.CommentParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentParserTest {

    private String param[];

    @Before
    public void setUp(){
        param = new String[]{
                "Test1.java",
                "Test2.java"
        };
    }

    @After
    public void tearDown(){
        param = null;
    }

    @Test
    public void testAction(){
        for(String s : param)
            assertEquals(0, CommentParser.parseComments(System.getProperty("user.dir") + "\\res\\" + s));
    }
}

package com.thumbtack.xonix.mind;
import org.junit.*;
import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.junit.runner.Runner;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.mockito.Mockito;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Runner.class)
public class TestApplication {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUp(){
        System.out.println("@BeforeClass");
    }

    @Test
    public void testAction(){
        Assert.assertTrue(true);
    }

    @AfterClass
    public static void tearDown(){
        System.out.println("@AfterClass");
    }
}

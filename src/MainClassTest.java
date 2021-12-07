import org.junit.Assert;
import org.junit.Test;

public class MainClassTest extends MainClass {

    @Test
    public void testGetLocalNumber() {

        int expectedValue = 14;
        int resultValue = this.getLocalNumber();
        String errorText = "Expected: " + expectedValue + " actually: " + resultValue;

        Assert.assertTrue(errorText, resultValue == expectedValue);
    }

    @Test
    public void testGetClassNumber() {

        int borderValue = 45;
        int resultValue = this.getClassNumber();
        String errorText = "Expected " + resultValue + " to be greater than " + borderValue;

        Assert.assertTrue(errorText, resultValue > borderValue);
    }

}
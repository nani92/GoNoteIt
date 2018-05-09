package eu.napcode.gonoteit.app.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InputValidatorTest {

    @Test
    public void shouldReturnTrueForNotEmptyInput() {
        InputValidator inputValidator = new InputValidator();
        boolean result = inputValidator.isInputNotEmpty("not empty");

        Assert.assertEquals(result, true);
    }

    @Test
    public void shouldReturnFalseForEmptyInput() {
        InputValidator inputValidator = new InputValidator();
        boolean result = inputValidator.isInputNotEmpty("");

        Assert.assertEquals(result, false);
    }

    @Test
    public void shouldReturnFalseForNull() {
        InputValidator inputValidator = new InputValidator();
        boolean result = inputValidator.isInputNotEmpty(null);

        Assert.assertEquals(result, false);
    }
}
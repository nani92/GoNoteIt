package eu.napcode.gonoteit.ui.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserValidatorTest {

    private UserValidator userValidator;

    @Before
    public void init() {
        userValidator = new UserValidator();
    }

    @Test
    public void testWithNotEmptyLoginAndPassword() {
        Assert.assertEquals(true, userValidator.isUserValid("user", "pass"));
    }

    @Test
    public void testWithEmptyLogin() {
        Assert.assertEquals(false, userValidator.isUserValid("", "pass"));
    }

    @Test
    public void testWithEmptyPassword() {
        Assert.assertEquals(false, userValidator.isUserValid("login", ""));
    }

    @Test
    public void testWithEmptyLoginAndPassword(){
        Assert.assertEquals(false, userValidator.isUserValid("", ""));
    }

    @Test
    public void testWithNullLogin(){
        Assert.assertEquals(false, userValidator.isUserValid(null, "pass"));
    }

    @Test
    public void testWithNullPass() {
        Assert.assertEquals(false, userValidator.isUserValid("login", null));
    }

    @Test
    public void testWithNullLoginAndPassword() {
        Assert.assertEquals(false, userValidator.isUserValid(null, null));
    }

    @Test
    public void testLoginValidation() {
        Assert.assertEquals(false, userValidator.isLoginValid(null));
        Assert.assertEquals(false, userValidator.isLoginValid(""));
        Assert.assertEquals(true, userValidator.isLoginValid("login"));
    }

    @Test
    public void testPasswordValidation() {
        Assert.assertEquals(false, userValidator.isPasswordValid(null));
        Assert.assertEquals(false, userValidator.isPasswordValid(""));
        Assert.assertEquals(true, userValidator.isPasswordValid("pass"));
    }

    @Test
    public void testTokenValidation() {
        Assert.assertEquals(false, userValidator.isTokenValid(null));
        Assert.assertEquals(false, userValidator.isTokenValid(""));
        Assert.assertEquals(true, userValidator.isTokenValid("token"));
    }
}
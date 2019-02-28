package eu.napcode.gonoteit.ui.login

import eu.napcode.gonoteit.app.utils.isLoginValid
import eu.napcode.gonoteit.app.utils.isPasswordValid
import eu.napcode.gonoteit.app.utils.isTokenValid
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import eu.napcode.gonoteit.app.utils.isUserValid

@RunWith(MockitoJUnitRunner::class)
class UserValidationTest {

    @Test
    fun testWithNotEmptyLoginAndPassword() {
        Assert.assertEquals(true, isUserValid("user", "pass"))
    }

    @Test
    fun testWithEmptyLogin() {
        Assert.assertEquals(false, isUserValid("", "pass"))
    }

    @Test
    fun testWithEmptyPassword() {
        Assert.assertEquals(false, isUserValid("login", ""))
    }

    @Test
    fun testWithEmptyLoginAndPassword() {
        Assert.assertEquals(false, isUserValid("", ""))
    }

    @Test
    fun testWithNullLogin() {
        Assert.assertEquals(false, isUserValid(null, "pass"))
    }

    @Test
    fun testWithNullPass() {
        Assert.assertEquals(false, isUserValid("login", null))
    }

    @Test
    fun testWithNullLoginAndPassword() {
        Assert.assertEquals(false, isUserValid(null, null))
    }

    @Test
    fun testLoginValidation() {
        Assert.assertEquals(false, isLoginValid(null))
        Assert.assertEquals(false, isLoginValid(""))
        Assert.assertEquals(true, isLoginValid("login"))
    }

    @Test
    fun testPasswordValidation() {
        Assert.assertEquals(false, isPasswordValid(null))
        Assert.assertEquals(false, isPasswordValid(""))
        Assert.assertEquals(true, isPasswordValid("pass"))
    }

    @Test
    fun testTokenValidation() {
        Assert.assertEquals(false, isTokenValid(null))
        Assert.assertEquals(false, isTokenValid(""))
        Assert.assertEquals(true, isTokenValid("token"))
    }
}
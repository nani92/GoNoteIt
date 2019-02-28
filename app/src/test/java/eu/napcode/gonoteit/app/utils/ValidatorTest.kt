package eu.napcode.gonoteit.app.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ValidatorTest {

    @Test
    fun shouldReturnTrueForNotEmptyInput() {
        val result = isInputNotEmpty("not empty")

        Assert.assertEquals(result, true)
    }

    @Test
    fun shouldReturnFalseForEmptyInput() {
        val result = isInputNotEmpty("")

        Assert.assertEquals(result, false)
    }

    @Test
    fun shouldReturnFalseForNull() {
        val result = isInputNotEmpty(null)

        Assert.assertEquals(result, false)
    }
}
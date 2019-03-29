package eu.napcode.gonoteit.ui.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

import com.apollographql.apollo.api.Response
import com.nhaarman.mockitokotlin2.mock

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.MockRxSchedulers
import eu.napcode.gonoteit.app.utils.isUserValid
import eu.napcode.gonoteit.repository.user.UserRepository
import io.reactivex.Observable

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    private var loginViewModel: LoginViewModel = mock()

    @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal var userRepository: UserRepository = mock()

    @Mock
    private val inputsValidationObserver: Observer<Boolean> = mock()

    @Mock
    internal var response: Response<AuthenticateMutation.Data> = mock()

    @Before
    fun init() {
        loginViewModel = LoginViewModel(userRepository, MockRxSchedulers())
    }

    @Test
    fun testForNotEmptyInput() {
        Mockito.`when`(isUserValid(Mockito.any(), Mockito.any()))
                .thenReturn(true)

        val areInputsValid = loginViewModel.areInputsValid()
        areInputsValid.observeForever(inputsValidationObserver)
        loginViewModel.setLogin("")

        Assert.assertEquals(true, areInputsValid.value)
    }

    @Test
    fun testForEmptyInput() {
        Mockito.`when`(isUserValid(Mockito.any(), Mockito.any()))
                .thenReturn(false)

        val areInputsValid = loginViewModel.areInputsValid()
        areInputsValid.observeForever(inputsValidationObserver)
        loginViewModel.setLogin("")

        Assert.assertEquals(false, areInputsValid.value)
    }

    @Test
    fun testLogin() {
        Mockito.`when`<Observable<Response<AuthenticateMutation.Data>>>(
                userRepository.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())
        ).thenReturn(Observable.just<Response<AuthenticateMutation.Data>>(response!!))

        loginViewModel.login()

        Mockito.verify<UserRepository>(userRepository)
                .authenticateUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())
    }
}
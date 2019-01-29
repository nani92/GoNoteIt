package eu.napcode.gonoteit.ui.login;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.apollographql.apollo.api.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.repository.user.UserRepository;
import io.reactivex.Observable;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    private LoginViewModel loginViewModel;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    @Mock
    UserValidator userValidator;

    @Mock
    private Observer<Boolean> inputsValidationObserver;

    @Mock
    Response<AuthenticateMutation.Data> response;

    @Before
    public void init() {
        loginViewModel = new LoginViewModel(userRepository, userValidator, new MockRxSchedulers());
    }

    @Test
    public void testForNotEmptyInput() {
        Mockito.when(userValidator.isUserValid(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        LiveData<Boolean> areInputsValid = loginViewModel.areInputsValid();
        areInputsValid.observeForever(inputsValidationObserver);
        loginViewModel.setLogin("");

        Assert.assertEquals(true, areInputsValid.getValue());
    }

    @Test
    public void testForEmptyInput() {
        Mockito.when(userValidator.isUserValid(Mockito.any(), Mockito.any()))
                .thenReturn(false);

        LiveData<Boolean> areInputsValid = loginViewModel.areInputsValid();
        areInputsValid.observeForever(inputsValidationObserver);
        loginViewModel.setLogin("");

        Assert.assertEquals(false, areInputsValid.getValue());
    }

    @Test
    public void testLogin() {
        Mockito.when(userRepository.authenticateUser(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Observable.just(response));

        loginViewModel.login();

        Mockito.verify(userRepository).authenticateUser(Mockito.any(), Mockito.any(), Mockito.any());
    }
}
package eu.napcode.gonoteit.ui.login;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import eu.napcode.gonoteit.repository.user.UserRepository;

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

    @Before
    public void init() {
        loginViewModel = new LoginViewModel(userRepository, userValidator);

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
}
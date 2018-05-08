package eu.napcode.gonoteit.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.user.UserRepository;

public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;
    private UserValidator userValidator;

    private String login, password;

    private MutableLiveData<Boolean> inputsValid = new MutableLiveData<>();

    @Inject
    public LoginViewModel(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;

        init();
    }

    private void init() {
        validate();
    }

    public LiveData<Boolean> areInputsValid() {
        return inputsValid;
    }

    public void setLogin(String login) {
        this.login = login;
        validate();
    }

    private void validate() {
        inputsValid.postValue(userValidator.isUserValid(login, password));
    }

    public void setPassword(String password) {
        this.password = password;
        validate();
    }
}
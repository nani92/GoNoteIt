package eu.napcode.gonoteit.ui.login;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.AuthenticateMutation;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.user.UserRepository;
import eu.napcode.gonoteit.rx.RxSchedulers;
import timber.log.Timber;

public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;
    private UserValidator userValidator;
    private RxSchedulers rxSchedulers;

    private String login, password;

    private MutableLiveData<Boolean> inputsValid = new MutableLiveData<>();
    private MutableLiveData<Resource<AuthenticateMutation.Data>> authenticationResource = new MutableLiveData<>();

    @Inject
    public LoginViewModel(UserRepository userRepository, UserValidator userValidator, RxSchedulers rxSchedulers) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.rxSchedulers = rxSchedulers;

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
        boolean valid = userValidator.isUserValid(login, password);
        Timber.d("Validated input %s and %s. Result: %b", login, password, valid);

        inputsValid.postValue(valid);
    }

    public void setPassword(String password) {
        this.password = password;
        validate();
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<Resource<AuthenticateMutation.Data>> login() {
        userRepository
                .authenticateUser(login, password, authenticationResource)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .doOnSubscribe(it -> authenticationResource.postValue(Resource.loading(null)))
                .doOnError(error -> {
                    authenticationResource.postValue(Resource.error(error));
                    Timber.d("Login error: %s", error.getLocalizedMessage());
                })
                .subscribe();

        return authenticationResource;
    }

}
package eu.napcode.gonoteit.ui.login;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.user.UserRepository;

public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;

    @Inject
    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

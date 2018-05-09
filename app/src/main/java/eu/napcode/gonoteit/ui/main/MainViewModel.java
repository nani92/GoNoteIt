package eu.napcode.gonoteit.ui.main;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.user.UserRepository;

public class MainViewModel extends ViewModel {

    private UserRepository userRepository;

    @Inject
    public MainViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
}

package eu.napcode.gonoteit.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.BuildConfig;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityLoginBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.repository.user.UserRepository;

public class LoginActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Inject
    UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        AndroidInjection.inject(this);

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(LoginViewModel.class);

        userRepository.authenticateUser(BuildConfig.API_LOGIN, BuildConfig.API_PASSWORD);
    }
}

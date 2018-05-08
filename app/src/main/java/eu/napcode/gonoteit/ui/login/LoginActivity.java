package eu.napcode.gonoteit.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.app.utils.SimpleTextWatcher;
import eu.napcode.gonoteit.databinding.ActivityLoginBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setupInputFields();

        AndroidInjection.inject(this);

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(LoginViewModel.class);

        this.viewModel.areInputsValid().observe(this,
                valid -> this.binding.loginButton.setEnabled(valid));
    }

    private void setupInputFields() {
        binding.passwordEditText.addTextChangedListener(inputWatcher);
        binding.loginEditText.addTextChangedListener(inputWatcher);
    }

    public TextWatcher inputWatcher = new SimpleTextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            viewModel.setLogin(binding.loginEditText.getText().toString());
            viewModel.setPassword(binding.passwordEditText.getText().toString());
        }
    };
}

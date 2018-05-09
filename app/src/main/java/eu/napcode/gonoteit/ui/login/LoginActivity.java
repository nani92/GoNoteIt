package eu.napcode.gonoteit.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.app.utils.SimpleTextWatcher;
import eu.napcode.gonoteit.databinding.ActivityLoginBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.ui.main.MainActivity;

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
        setupViewModel();
    }

    private void setupInputFields() {
        binding.passwordEditText.addTextChangedListener(inputWatcher);
        binding.loginEditText.addTextChangedListener(inputWatcher);

        binding.passwordEditText.setOnEditorActionListener((view, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEND) {
                login();

                return true;
            }

            return false;
        });

        this.binding.loginButton.setOnClickListener(view -> login());
    }

    private void login() {
        this.viewModel.login().observe(this, resource -> {

            if (resource.status == Resource.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.loginButton.setEnabled(false);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);
            }

            if (resource.status == Resource.Status.SUCCESS) {
                startActivity(new Intent(this, MainActivity.class));
                finish();

                return;
            }

            if (resource.status == Resource.Status.ERROR) {
                String message = resource.message;

                if (resource.message == null) {
                    message = getString(R.string.login_error);
                }

                Snackbar.make(binding.constraintLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public TextWatcher inputWatcher = new SimpleTextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            viewModel.setLogin(binding.loginEditText.getText().toString());
            viewModel.setPassword(binding.passwordEditText.getText().toString());
        }
    };

    private void setupViewModel() {
        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(LoginViewModel.class);

        this.viewModel.areInputsValid().observe(this,
                valid -> this.binding.loginButton.setEnabled(valid));
    }
}

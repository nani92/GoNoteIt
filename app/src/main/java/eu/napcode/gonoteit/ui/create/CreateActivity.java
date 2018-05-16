package eu.napcode.gonoteit.ui.create;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityCreateBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.ui.main.MainViewModel;

public class CreateActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private ActivityCreateBinding binding;
    private CreateViewModel createViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create);

        AndroidInjection.inject(this);

        this.createViewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(CreateViewModel.class);
    }
}

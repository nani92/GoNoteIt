package eu.napcode.gonoteit.ui.note;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityNoteBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;

public class NoteActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    ActivityNoteBinding binding;
    private NoteViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);

        AndroidInjection.inject(this);

        setupViewModel();
    }

    void setupViewModel() {
        this.viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NoteViewModel.class);
    }
}

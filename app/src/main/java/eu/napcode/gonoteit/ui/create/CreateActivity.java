package eu.napcode.gonoteit.ui.create;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityCreateBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource.Status;
import eu.napcode.gonoteit.repository.Resource;

public class CreateActivity extends AppCompatActivity {

    //TODO: add back nav

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

        binding.createNoteButton.setOnClickListener(v ->
                createViewModel.createNote(getNoteModelFromInputs())
                .observe(this, this::processResponse)
        );
    }

    private void processResponse(Resource resource) {
        updateForLoading(resource.status == Status.LOADING);

        if (resource.status == Status.SUCCESS) {
            finish();

            return;
        }

        if (resource.status == Status.ERROR) {
            showError(resource.message);
        }
    }

    private void updateForLoading(boolean loading) {

        if (loading) {
            this.binding.createNoteButton.setEnabled(false);
            this.binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.binding.createNoteButton.setEnabled(true);
            this.binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {

        if (message == null) {
            message = getString(R.string.error_with_saving_note);
        }

        Snackbar.make(binding.constraintLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private NoteModel getNoteModelFromInputs() {
        NoteModel noteModel = new NoteModel();
        noteModel.setTitle(binding.titleEditText.getText().toString());
        noteModel.setContent(binding.contentEditText.getText().toString());

        return noteModel;
    }
}

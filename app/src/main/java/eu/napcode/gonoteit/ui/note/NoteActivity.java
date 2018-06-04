package eu.napcode.gonoteit.ui.note;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityNoteBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.utils.ImageUtils;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static eu.napcode.gonoteit.repository.Resource.Status.ERROR;
import static eu.napcode.gonoteit.repository.Resource.Status.LOADING;
import static eu.napcode.gonoteit.repository.Resource.Status.SUCCESS;

public class NoteActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    ActivityNoteBinding binding;
    private NoteViewModel viewModel;

    public static final String NOTE_ID_KEY = "note id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);

        AndroidInjection.inject(this);

        setupViewModel();
        getNote();
    }

    void setupViewModel() {
        this.viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NoteViewModel.class);
    }

    private void getNote() {
        Long id = getIntent().getLongExtra(NOTE_ID_KEY, 0);

        this.viewModel.getNote(id).observe(this, this::processNote);
    }

    private void processNote(Resource<NoteModel> noteModelResource) {
        boolean loading = noteModelResource.status == LOADING;
        binding.progressBar.setVisibility(loading ? VISIBLE : GONE);

        if (noteModelResource.status == ERROR) {
            showError(noteModelResource.message);
        }

        if (noteModelResource.status == SUCCESS) {
            displayNote(noteModelResource.data);
        }
    }

    private void showError(String message) {

        if (message == null) {
            message = getString(R.string.error_with_saving_note);
        }

        Snackbar.make(binding.constraintLayout, message, LENGTH_LONG).show();
    }


    private void displayNote(NoteModel noteModel) {
        displayTextInTextView(noteModel.getTitle(), binding.noteTitleTextView);
        displayTextInTextView(noteModel.getContent(), binding.noteTextView);

        if (!TextUtils.isEmpty(noteModel.getImageBase64())) {
            displayImage(noteModel);
        }
    }

    private void displayTextInTextView(String text, TextView textView) {

        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        } else {
            textView.setText(text);
        }
    }

    private void displayImage(NoteModel noteModel) {
        Glide.with(this)
                .load(ImageUtils.decodeBase64ToBitmap(noteModel.getImageBase64()))
                .into(binding.imageView);

        binding.imageView.setVisibility(VISIBLE);
    }

}

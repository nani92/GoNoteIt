package eu.napcode.gonoteit.ui.create;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.databinding.ActivityCreateBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource.Status;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.utils.ImageUtils;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.graphics.Bitmap.CompressFormat.JPEG;

public class CreateActivity extends AppCompatActivity {

    //TODO: add back nav

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    Tracker tracker;

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

        binding.createNoteButton.setOnClickListener(v -> {
                    createViewModel.createNote(getNoteModelFromInputs())
                            .observe(this, this::processResponse);

                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Create note")
                            .build());
                }
        );

        binding.addImageButton.setOnClickListener(v ->
                EasyImage.openGallery(this, 0)
        );


        tracker.setScreenName("Create note screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
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
        setImageForNote(noteModel);

        return noteModel;
    }

    private void setImageForNote(NoteModel noteModel) {

        if (binding.attachmentCardView.getVisibility() == View.GONE) {
            return;
        }

        BitmapDrawable imageDrawable = (BitmapDrawable) binding.attachmentImageView.getDrawable();
        noteModel.setImageBase64(ImageUtils.encodeBitmapToBase64(imageDrawable.getBitmap(), JPEG, 100));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, getImageCallback);
    }

    private DefaultCallback getImageCallback = new DefaultCallback() {

        @Override
        public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
            Snackbar.make(binding.constraintLayout, R.string.error_with_capture_image, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {

            if (imagesFiles != null && imagesFiles.size() > 0) {
                displayImage(imagesFiles.get(0));
            } else {
                Snackbar.make(binding.constraintLayout, R.string.error_image_not_found, Snackbar.LENGTH_LONG).show();
            }
        }
    };

    private void displayImage(File file) {
        Glide.with(this)
                .load(file)
                .into(binding.attachmentImageView);

        binding.attachmentCardView.setVisibility(View.VISIBLE);
    }

}

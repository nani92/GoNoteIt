package eu.napcode.gonoteit.ui.create;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.data.results.NoteResult;
import eu.napcode.gonoteit.databinding.ActivityCreateBinding;
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource.Status;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.type.Access;
import eu.napcode.gonoteit.utils.GlideBase64Loader;
import eu.napcode.gonoteit.utils.ImageUtils;
import eu.napcode.gonoteit.utils.RevealActivityHelper;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static android.view.View.VISIBLE;
import static eu.napcode.gonoteit.repository.Resource.Status.ERROR;
import static eu.napcode.gonoteit.ui.create.PermsExplanationHelperKt.getReadPermsExplanation;
import static eu.napcode.gonoteit.ui.create.PermsExplanationHelperKt.getWritePermsExplanation;
import static eu.napcode.gonoteit.utils.RevealActivityHelper.REVEAL_X_KEY;
import static eu.napcode.gonoteit.utils.RevealActivityHelper.REVEAL_Y_KEY;

public class CreateActivity extends AppCompatActivity implements PermissionsDialogFragment.PermissionsDialogListener {

    //TODO: add back nav

    public static final String EDIT_NOTE_ID_KEY = "edit id";

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    GlideBase64Loader glideBase64Loader;

    private ActivityCreateBinding binding;
    private CreateViewModel viewModel;

    private static int PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 303;
    private static String IMAGE_STATE_KEY = "image";

    private Access readPermissions = Access.INTERNAL;
    private Access writePermissions = Access.INTERNAL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create);

        AndroidInjection.inject(this);

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(CreateViewModel.class);

        setupAnimations();
        setupViews();

        if (savedInstanceState != null) {
            displaySavedImage(savedInstanceState);
        }

        if (isInEditMode() && savedInstanceState == null) {
            getNoteToEdit();
        }

        if (isInEditMode()) {
            binding.createNoteButton.setText(R.string.update_note);
        }
    }

    private void setupAnimations() {

        if (shouldDisplayCircularReveal()) {
            new RevealActivityHelper(this, binding.constraintLayout, getIntent());
        } else {
            binding.constraintLayout.setVisibility(VISIBLE);
            setupEnterTransition();
        }

        setupReturnTransition();
    }

    private boolean shouldDisplayCircularReveal() {
        return getIntent().hasExtra(REVEAL_X_KEY) && getIntent().hasExtra(REVEAL_Y_KEY);
    }

    private void setupEnterTransition() {
        Slide slide = new Slide();
        slide.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        getWindow().setEnterTransition(slide);
    }

    private void setupReturnTransition() {
        Explode explode = new Explode();
        explode.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        getWindow().setReturnTransition(explode);
    }

    private void setupViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.createNoteButton.setOnClickListener(v -> {
                    LiveData<Resource> createResource = viewModel.createNote(getNoteModelFromInputs());
                    createResource.observe(this, resource -> processResponseCreateNote(resource));
                }
        );

        binding.addImageButton.setOnClickListener(v ->
                getImageFromGallery()
        );

        binding.removeImageButton.setOnClickListener(v -> {
            binding.attachmentCardView.setVisibility(View.GONE);
            binding.attachmentImageView.setImageDrawable(null);
        });

        displayPermsExplanation();
        binding.permsExplanationTextView.setOnClickListener(view -> showPermDialog());
    }

    private void displayPermsExplanation() {
        binding.permsExplanationTextView.setText(String.format("%s \n%s",
                getReadPermsExplanation(this, readPermissions),
                getWritePermsExplanation(this, writePermissions)));
    }

    private void getImageFromGallery() {

        if (hasPermissions()) {
            EasyImage.openGallery(this, 0);
        } else {
            askForPermissions();
        }
    }

    private boolean hasPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void askForPermissions() {
        //TODO add rationale when needed
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
    }

    private void displaySavedImage(Bundle savedInstanceState) {
        Bitmap bitmap = savedInstanceState.getParcelable(IMAGE_STATE_KEY);

        if (bitmap == null) {
            return;
        }

        Glide.with(this)
                .load(bitmap)
                .into(binding.attachmentImageView);
        binding.attachmentCardView.setVisibility(VISIBLE);
    }

    private boolean isInEditMode() {
        return getIntent().hasExtra(EDIT_NOTE_ID_KEY);
    }

    private void getNoteToEdit() {
        NoteResult noteResult = viewModel.getNote(getNoteToEditId());

        noteResult.getNote().observe(this, this::displayNote);
        noteResult.getResource().observe(this, resource ->
                processResponseGetNoteToEdit(resource, noteResult.getResource()));
    }

    private Long getNoteToEditId() {
        return getIntent().getExtras().getLong(EDIT_NOTE_ID_KEY);
    }

    private void displayNote(NoteModel noteModel) {
        binding.createNoteButton.setText(R.string.update_note);
        binding.titleEditText.setText(noteModel.getTitle());
        binding.contentEditText.setText(noteModel.getContent());
        this.readPermissions = noteModel.getReadAccess();
        this.writePermissions = noteModel.getWriteAccess();
        displayPermsExplanation();

        if (TextUtils.isEmpty(noteModel.getImageBase64()) == false) {
            displayImage(noteModel);
        }
    }

    private void displayImage(NoteModel noteModel) {
        glideBase64Loader.loadBase64IntoView(noteModel.getImageBase64(), binding.attachmentImageView);
        binding.attachmentCardView.setVisibility(VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        if (item.getItemId() == R.id.perms) {
            showPermDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPermDialog() {
        PermissionsDialogFragment dialog = PermissionsDialogFragment
                .Companion
                .newInstance(readPermissions, writePermissions);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void processResponseCreateNote(Resource resource) {
        updateForLoading(resource.status == Status.LOADING);

        if (resource.status == Status.SUCCESS) {
            finish();

            return;
        }

        if (resource.status == ERROR) {
            showError(resource.message);
        }
    }

    private void processResponseGetNoteToEdit(Resource resource, LiveData<Resource> resourceLiveData) {
        updateForLoading(resource.status == Status.LOADING);

        if (resource.status == ERROR) {
            showError(resource.message);
        }
    }

    private void updateForLoading(boolean loading) {

        if (loading) {
            this.binding.createNoteButton.setEnabled(false);
            this.binding.progressBar.setVisibility(VISIBLE);
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
        noteModel.setReadAccess(readPermissions);
        noteModel.setWriteAccess(writePermissions);

        if (isInEditMode()) {
            noteModel.setId(getNoteToEditId());
        }

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
                displayImageFile(imagesFiles.get(0));
            } else {
                Snackbar.make(binding.constraintLayout, R.string.error_image_not_found, Snackbar.LENGTH_LONG).show();
            }
        }
    };

    private void displayImageFile(File file) {
        Glide.with(this)
                .load(file)
                .into(binding.attachmentImageView);

        binding.attachmentCardView.setVisibility(VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_EXTERNAL_STORAGE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery();
            }

            //TODO else display info that we're not able to grab a photo
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveBitmapToOutState(outState);

        super.onSaveInstanceState(outState);
    }

    private void saveBitmapToOutState(Bundle outState) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.attachmentImageView.getDrawable();

        if (bitmapDrawable == null) {
            return;
        }

        outState.putParcelable(IMAGE_STATE_KEY, bitmapDrawable.getBitmap());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);

        return true;
    }

    @Override
    public void onChangedReadPerms(@NotNull Access readAccess) {
        this.readPermissions = readAccess;
    }

    @Override
    public void onChangedWritePerms(@NotNull Access writeAccess) {
        this.writePermissions = writeAccess;
    }
}

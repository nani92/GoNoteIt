package eu.napcode.gonoteit.ui.create

import android.Manifest
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Explode
import android.transition.Slide
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager

import com.bumptech.glide.Glide

import java.io.File

import javax.inject.Inject

import dagger.android.AndroidInjection
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.repository.Resource.Status
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.type.Access
import eu.napcode.gonoteit.utils.GlideBase64Loader
import eu.napcode.gonoteit.utils.ImageUtils
import eu.napcode.gonoteit.utils.RevealActivityHelper
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage

import android.graphics.Bitmap.CompressFormat.JPEG
import android.view.View.VISIBLE
import eu.napcode.gonoteit.repository.Resource.Status.ERROR
import eu.napcode.gonoteit.utils.RevealActivityHelper.REVEAL_X_KEY
import eu.napcode.gonoteit.utils.RevealActivityHelper.REVEAL_Y_KEY
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity(), PermissionsDialogFragment.PermissionsDialogListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var glideBase64Loader: GlideBase64Loader

    private var viewModel: CreateViewModel? = null

    private var readPermissions = Access.INTERNAL
    private var writePermissions = Access.INTERNAL

    private val isInEditMode: Boolean
        get() = intent.hasExtra(EDIT_NOTE_ID_KEY)

    private val noteToEditId: Long
        get() = intent.extras!!.getLong(EDIT_NOTE_ID_KEY)

    private val noteModelFromInputs: NoteModel
        get() {
            val noteModel = NoteModel()
            noteModel.title = titleEditText.text.toString()
            noteModel.content = contentEditText.text.toString()
            setImageForNote(noteModel)
            noteModel.readAccess = readPermissions
            noteModel.writeAccess = writePermissions

            if (isInEditMode) {
                noteModel.id = noteToEditId
            }

            return noteModel
        }

    private val getImageCallback = object : DefaultCallback() {

        override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
            Snackbar.make(constraintLayout, R.string.error_with_capture_image, Snackbar.LENGTH_LONG).show()
        }

        override fun onImagesPicked(imagesFiles: List<File>, source: EasyImage.ImageSource, type: Int) {

            if (imagesFiles != null && imagesFiles.size > 0) {
                displayImageFile(imagesFiles[0])
            } else {
                Snackbar.make(constraintLayout, R.string.error_image_not_found, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContentView(R.layout.activity_create)

        AndroidInjection.inject(this)

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(CreateViewModel::class.java)

        setupAnimations()
        setupViews()

        if (savedInstanceState != null) {
            displaySavedImage(savedInstanceState)
        }

        if (isInEditMode) {
            getNoteToEdit()
        }
    }

    private fun setupAnimations() {

        if (shouldDisplayCircularReveal()) {
            RevealActivityHelper(this, constraintLayout, intent)
        } else {
            constraintLayout.visibility = VISIBLE
            setupEnterTransition()
        }

        setupReturnTransition()
    }

    private fun shouldDisplayCircularReveal(): Boolean {
        return intent.hasExtra(REVEAL_X_KEY) && intent.hasExtra(REVEAL_Y_KEY)
    }

    private fun setupEnterTransition() {
        val slide = Slide()
        slide.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()

        window.enterTransition = slide
    }

    private fun setupReturnTransition() {
        val explode = Explode()
        explode.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()

        window.returnTransition = explode
    }

    private fun setupViews() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        createNoteButton.setOnClickListener { v ->
            val createResource = viewModel!!.createNote(noteModelFromInputs)
            createResource.observe(this,
                    Observer { resource -> processResponseCreateNote(resource!!) }
            )
        }

        addImageButton.setOnClickListener { v -> getImageFromGallery() }

        removeImageButton.setOnClickListener { v ->
            attachmentCardView.visibility = View.GONE
            attachmentImageView.setImageDrawable(null)
        }

        displayPermsExplanation()
        permsExplanationTextView.setOnClickListener { view -> showPermDialog() }
    }

    private fun displayPermsExplanation() {
        permsExplanationTextView.text = String.format("%s \n%s",
                getReadPermsExplanation(this, readPermissions),
                getWritePermsExplanation(this, writePermissions))
    }

    private fun getImageFromGallery() {

        if (hasPermissions()) {
            EasyImage.openGallery(this, 0)
        } else {
            askForPermissions()
        }
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions() {
        //TODO add rationale when needed
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_EXTERNAL_STORAGE)
    }

    private fun displaySavedImage(savedInstanceState: Bundle) {
        val bitmap = savedInstanceState.getParcelable<Bitmap>(IMAGE_STATE_KEY) ?: return

        Glide.with(this)
                .load(bitmap)
                .into(attachmentImageView)
        attachmentCardView.visibility = VISIBLE
    }

    private fun getNoteToEdit() {
        val noteResult = viewModel!!.getNote(noteToEditId)

        noteResult.note.observe(this, Observer<NoteModel> { this.displayNote(it!!) })
        noteResult.resource.observe(this, Observer { resource -> processResponseGetNoteToEdit(resource!!, noteResult.resource) })
    }

    private fun displayNote(noteModel: NoteModel) {
        createNoteButton.setText(R.string.update_note)
        titleEditText.setText(noteModel.title)
        contentEditText.setText(noteModel.content)
        this.readPermissions = noteModel.readAccess
        this.writePermissions = noteModel.writeAccess
        displayPermsExplanation()

        if (TextUtils.isEmpty(noteModel.imageBase64) == false) {
            displayImage(noteModel)
        }
    }

    private fun displayImage(noteModel: NoteModel) {
        glideBase64Loader!!.loadBase64IntoView(noteModel.imageBase64, attachmentImageView)
        attachmentCardView.visibility = VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this)

            return true
        }

        if (item.itemId == R.id.perms) {
            showPermDialog()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showPermDialog() {
        val dialog = PermissionsDialogFragment
                .newInstance(readPermissions, writePermissions)
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog)
        dialog.show(supportFragmentManager, "")
    }

    private fun processResponseCreateNote(resource: Resource<Any>) {
        updateForLoading(resource.status == Status.LOADING)

        if (resource.status == Status.SUCCESS) {
            finish()

            return
        }

        if (resource.status == ERROR) {
            showError(resource.message)
        }
    }

    private fun processResponseGetNoteToEdit(resource: Resource<Any>, resourceLiveData: LiveData<Resource<*>>) {
        updateForLoading(resource.status == Status.LOADING)

        if (resource.status == ERROR) {
            showError(resource.message)
        }
    }

    private fun updateForLoading(loading: Boolean) {

        if (loading) {
            createNoteButton.isEnabled = false
            progressBar.visibility = VISIBLE
        } else {
            createNoteButton.isEnabled = true
            progressBar.visibility = View.GONE
        }
    }

    private fun showError(message: String?) {
        var message = message

        if (message == null) {
            message = getString(R.string.error_with_saving_note)
        }

        Snackbar.make(constraintLayout, message!!, Snackbar.LENGTH_LONG).show()
    }

    private fun setImageForNote(noteModel: NoteModel) {

        if (attachmentCardView.visibility == View.GONE) {
            return
        }

        val imageDrawable = attachmentImageView.drawable as BitmapDrawable
        noteModel.imageBase64 = ImageUtils.encodeBitmapToBase64(imageDrawable.bitmap, JPEG, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, getImageCallback)
    }

    private fun displayImageFile(file: File) {
        Glide.with(this)
                .load(file)
                .into(attachmentImageView)

        attachmentCardView.visibility = VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == PERMISSIONS_REQUEST_EXTERNAL_STORAGE) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery()
            }

            //TODO else display info that we're not able to grab a photo
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveBitmapToOutState(outState)

        super.onSaveInstanceState(outState)
    }

    private fun saveBitmapToOutState(outState: Bundle) {
        val bitmapDrawable = attachmentImageView.drawable as BitmapDrawable ?: return

        outState.putParcelable(IMAGE_STATE_KEY, bitmapDrawable.bitmap)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)

        return true
    }

    override fun onChangedReadPerms(readAccess: Access) {
        this.readPermissions = readAccess
    }

    override fun onChangedWritePerms(writeAccess: Access) {
        this.writePermissions = writeAccess
    }

    companion object {

        //TODO: add back nav

        val EDIT_NOTE_ID_KEY = "edit id"

        private val PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 303
        private val IMAGE_STATE_KEY = "image"
    }
}

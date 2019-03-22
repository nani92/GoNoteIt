package eu.napcode.gonoteit.ui.note

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import javax.inject.Inject

import dagger.android.AndroidInjection
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.ui.create.CreateActivity
import eu.napcode.gonoteit.utils.GlideBase64Loader

import android.support.design.widget.Snackbar.LENGTH_LONG
import android.view.View.GONE
import android.view.View.VISIBLE
import eu.napcode.gonoteit.repository.Resource.Status.ERROR
import eu.napcode.gonoteit.repository.Resource.Status.LOADING
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var glideBase64Loader: GlideBase64Loader

    private var viewModel: NoteViewModel? = null
    private var favoriteImageView: ImageView? = null

    private val bundleForUpdate: Bundle
        get() {
            val bundle = Bundle()
            bundle.putLong(CreateActivity.EDIT_NOTE_ID_KEY, intent.getLongExtra(NOTE_ID_KEY, 0))

            return bundle
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        AndroidInjection.inject(this)

        setupViewModel()
        getNote()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)

        createFavoriteImageView()
        menu.getItem(0).actionView = favoriteImageView

        return true
    }

    private fun createFavoriteImageView() {

        if (favoriteImageView != null) {
            return
        }

        favoriteImageView = ImageView(this)
        favoriteImageView!!.setImageResource(R.drawable.ic_favorite_24px)
        favoriteImageView!!.setColorFilter(Color.argb(255, 255, 255, 255))
        favoriteImageView!!.setOnClickListener { v -> viewModel!!.updateFavorites(intent.getLongExtra(NOTE_ID_KEY, 0)) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.update) {
            showUpdateScreen()
        }

        if (item.itemId == R.id.favourite) {
            viewModel!!.updateFavorites(intent.getLongExtra(NOTE_ID_KEY, 0))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showUpdateScreen() {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)

        val intent = Intent(this, CreateActivity::class.java)
        intent.putExtras(bundleForUpdate)

        startActivity(intent, options.toBundle())
    }

    private fun setupViewModel() {
        this.viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NoteViewModel::class.java)
    }

    private fun getNote() {
        val id = intent.getLongExtra(NOTE_ID_KEY, 0)
        val noteResult = this.viewModel!!.getNote(id)

        noteResult.note.observe(this, Observer<NoteModel> { this.displayNote(it!!) })
        noteResult.resource.observe(this, Observer { this.processNote(it!!) })

        this.viewModel!!.isNoteFavorite(id).observe(this, Observer<Boolean> { this.displayFavorite(it!!) })
    }

    private fun displayFavorite(isFavorite: Boolean) {
        createFavoriteImageView()

        if (isFavorite) {
            favoriteImageView!!.setImageResource(R.drawable.fav_empty_to_full)
        } else {
            favoriteImageView!!.setImageResource(R.drawable.fav_full_to_empty)
        }

        (this.favoriteImageView!!.drawable as Animatable).start()
    }

    private fun processNote(resource: Resource<*>) {
        val loading = resource.status == LOADING
        progressBar.visibility = if (loading) VISIBLE else GONE

        if (resource.status == ERROR) {
            showError(resource.message)
        }
    }

    private fun showError(message: String?) {
        var message = message

        if (message == null) {
            message = getString(R.string.error_with_saving_note)
        }

        Snackbar.make(constraintLayout, message!!, LENGTH_LONG).show()
    }


    private fun displayNote(noteModel: NoteModel) {
        displayTextInTextView(noteModel.title, noteTitleTextView)
        displayTextInTextView(noteModel.content, noteTextView)

        if (!TextUtils.isEmpty(noteModel.imageBase64)) {
            displayImage(noteModel)
        } else {
            imageView.visibility = GONE
        }
    }

    private fun displayTextInTextView(text: String?, textView: TextView) {

        if (TextUtils.isEmpty(text)) {
            textView.visibility = GONE
        } else {
            textView.text = text
            textView.visibility = VISIBLE
        }
    }

    private fun displayImage(noteModel: NoteModel) {
        glideBase64Loader!!.loadBase64IntoView(noteModel.imageBase64, imageView)

        imageView.visibility = VISIBLE
    }

    companion object {
        val NOTE_ID_KEY = "note id"
    }
}

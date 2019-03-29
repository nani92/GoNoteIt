package eu.napcode.gonoteit.ui.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.ui.create.CreateActivity
import eu.napcode.gonoteit.ui.note.NoteActivity

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import eu.napcode.gonoteit.repository.Resource.Status.ERROR
import eu.napcode.gonoteit.ui.main.MainActivityProgressBarManager.manageProgressBarDisplaying
import eu.napcode.gonoteit.ui.note.NoteActivity.Companion.NOTE_ID_KEY
import eu.napcode.gonoteit.utils.RevealActivityHelper.REVEAL_X_KEY
import eu.napcode.gonoteit.utils.RevealActivityHelper.REVEAL_Y_KEY
import kotlinx.android.synthetic.main.fragment_notes.*
import eu.napcode.gonoteit.R


class NotesFragment : Fragment(), NotesAdapter.NoteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var viewModel: NotesViewModel? = null
    private var notesAdapter: NotesAdapter? = null

    internal var recyclerViewLoadAnimationDisplayed: Boolean = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(NotesViewModel::class.java)

        setupViews()
        subscribeToNotes()
    }

    private fun subscribeToNotes() {
        val notesResult = if (arguments!!.getBoolean(SHOULD_DISPLAY_ONLY_FAV_NOTES)) {
            this.viewModel!!.favoriteNotes
        } else {
            this.viewModel!!.notes
        }
        notesResult.notes.observe(this, Observer<PagedList<NoteModel>> { this.displayNotes(it) })
        notesResult.resource.observe(this, Observer<Resource<*>> { this.processResource(it!!) })
    }

    private fun displayNotes(noteModels: PagedList<NoteModel>?) {
        notesAdapter!!.submitList(noteModels)

        if (recyclerViewLoadAnimationDisplayed == false) {
            recyclerViewLoadAnimationDisplayed = true
            recyclerView.scheduleLayoutAnimation()
        }
    }

    private fun processResource(resource: Resource<*>) {
        manageProgressBarDisplaying(activity, resource.status)

        if (resource.status == ERROR) {
            displayMessage(resource.message)
        }
    }

    private fun displayMessage(message: String?) {
        var message = message

        if (message == null || message.isEmpty()) {
            message = getString(R.string.general_error_downloading)
        }

        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun setupViews() {
        setupRecyclerView()

        createFab.setOnClickListener { v -> displayCreateActivity() }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            viewModel!!.refresh()
        }
    }

    private fun displayCreateActivity() {
        val intent = Intent(this@NotesFragment.context, CreateActivity::class.java)
        intent.putExtra(REVEAL_X_KEY, revealXForCreateActivity)
        intent.putExtra(REVEAL_Y_KEY, revealYForCreateActivity)

        startActivity(intent, optionsForCreateActivity)
    }

    private fun setupRecyclerView() {
        setupLayoutManager()

        this.notesAdapter = NotesAdapter(context, this)
        recyclerView.adapter = notesAdapter

        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation)
    }

    private fun setupLayoutManager() {

        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(context, LANDSCAPE_COLUMNS)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel!!.notes
    }

    override fun onDeleteNote(id: Long?) {
        val deletedResult = viewModel!!.deleteNote(id)
        deletedResult.resource.observe(this, Observer<Resource<*>> { this.processDeleteResponse(it!!) })
    }

    override fun onClickNote(noteModel: NoteModel, vararg sharedElementPairs: Pair<View, String>) {
        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra(NOTE_ID_KEY, noteModel.id)

        startActivity(intent, getAnimationBundle(*sharedElementPairs))
    }

    override fun onNoteListChanged() {
        (recyclerView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(0, 0)
    }

    private fun getAnimationBundle(vararg sharedElementPairs: Pair<View, String>): Bundle? {
        val optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!, *sharedElementPairs)

        return optionsCompat.toBundle()
    }

    private fun processDeleteResponse(booleanResource: Resource<*>) {
        manageProgressBarDisplaying(activity, booleanResource.status)

        if (booleanResource.status == ERROR) {
            displayMessage(booleanResource.message)
        }
    }

    private val optionsForCreateActivity: Bundle?
        get() {
            val transitionName = getString(R.string.transition_circular_reveal_create_screen)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, createFab, transitionName)

            return options.toBundle()
        }

    private val revealXForCreateActivity: Int
        get() = (createFab.x + createFab.width / 2).toInt()

    private val revealYForCreateActivity: Int
        get() = (createFab.y + createFab.height / 2).toInt()


    companion object {
        private val LANDSCAPE_COLUMNS = 2

        private val SHOULD_DISPLAY_ONLY_FAV_NOTES = "should display fav"

        public fun newInstance(shouldDisplayFav: Boolean) = NotesFragment().apply {
            arguments = Bundle(2).apply {
                putBoolean(SHOULD_DISPLAY_ONLY_FAV_NOTES, shouldDisplayFav)
            }
        }
    }
}

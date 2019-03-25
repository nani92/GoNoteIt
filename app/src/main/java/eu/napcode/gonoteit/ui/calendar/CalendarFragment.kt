package eu.napcode.gonoteit.ui.calendar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.ui.notes.NotesFragment

class CalendarFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var viewModel: CalendarViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(CalendarViewModel::class.java)

        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        var calendarResult = this.viewModel!!.getTodayEvents()

        calendarResult.notes.observe(this, Observer<List<NoteModel>> { Log.d("Natalia", "list" + it!!.size) })
        //calendarResult.resource.observe(this, Observer<Resource<*>> { this.processResource(it!!) })
    }
}

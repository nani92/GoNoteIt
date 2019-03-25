package eu.napcode.gonoteit.ui.calendar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.ui.notes.NotesFragment
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class CalendarFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var viewModel: CalendarViewModel? = null

    private var calendarAdapter: CalendarAdapter? = null

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

        setupRecyclerView()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        var calendarResult = this.viewModel!!.getTodayEvents()

        calendarResult.notes.observe(this, Observer<List<NoteModel>> {
            val calendarElements = mutableListOf<CalendarAdapterElement>()
            var calendar = Calendar.getInstance()
            calendar.time = Date()
            calendarElements.add(CalendarAdapterElement(true, null, calendar))

            it!!.forEach { noteModel ->
                calendarElements.add(CalendarAdapterElement(false, noteModel, null))
            }

            calendarAdapter!!.calendarElements = calendarElements
        })
        //calendarResult.resource.observe(this, Observer<Resource<*>> { this.processResource(it!!) })
    }

    private fun setupRecyclerView() {
        calendarRecyclerView.layoutManager = LinearLayoutManager(context)

        this.calendarAdapter = CalendarAdapter()
        calendarRecyclerView.adapter = calendarAdapter

        calendarRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation)
    }
}

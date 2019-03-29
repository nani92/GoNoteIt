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
import eu.napcode.gonoteit.utils.isSameDate
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
    }

    override fun onStart() {
        super.onStart()

        setupRecyclerView()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        var calendarResult = this.viewModel!!.getWeekEvents()

        calendarResult.notes.observe(this, Observer<List<NoteModel>> {
            calendarAdapter!!.calendarElements = getCalendarElementsFromResult(it!!)
        })
    }

    private fun getCalendarElementsFromResult(notes: List<NoteModel>) : List<CalendarAdapterElement> {
        val calendarElements = mutableListOf<CalendarAdapterElement>()

        addTodayDateIfShould(calendarElements, notes)

        notes.forEach { noteModel ->
            if (hasDate(calendarElements, noteModel.date!!) == false) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = noteModel.date!!
                calendarElements.add(CalendarAdapterElement(true, null, calendar))
            }

            calendarElements.add(CalendarAdapterElement(false, noteModel, null))
        }

        return calendarElements
    }

    private fun addTodayDateIfShould(list: MutableList<CalendarAdapterElement>, notes: List<NoteModel>) {
        val firstCalendar = Calendar.getInstance()
        firstCalendar.timeInMillis = notes[0].date!!

        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = Date()

        if (isSameDate(firstCalendar, todayCalendar)) {
            return
        }

        list.add(CalendarAdapterElement(true, null, todayCalendar))
        list.add(CalendarAdapterElement(false, null, null))
    }

    private fun hasDate(list: List<CalendarAdapterElement>, dateTimestamp: Long) : Boolean {
        val date = Calendar.getInstance()
        date.timeInMillis = dateTimestamp

        val filter = list.filter {

            if (!it.isDate) {
                return@filter false
            }

            isSameDate(date, it.date!!)
        }

        return filter.isNotEmpty()
    }

    private fun setupRecyclerView() {
        calendarRecyclerView.layoutManager = LinearLayoutManager(context)

        this.calendarAdapter = CalendarAdapter(this.context!!)
        calendarRecyclerView.adapter = calendarAdapter

        calendarRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_view_animation)
    }
}

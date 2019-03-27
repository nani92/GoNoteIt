package eu.napcode.gonoteit.repository.calendar

import android.arch.lifecycle.MutableLiveData
import eu.napcode.gonoteit.data.calendar.CalendarLocal
import eu.napcode.gonoteit.data.calendar.CalendarResult
import eu.napcode.gonoteit.data.notes.results.NotesResult
import eu.napcode.gonoteit.repository.Resource
import javax.inject.Inject

class CalendarRepositoryImpl @Inject
constructor(private val calendarLocal: CalendarLocal) : CalendarRepository {

    internal var resource = MutableLiveData<Resource<*>>()

    override fun getTodayEvents(): CalendarResult {
        return CalendarResult(calendarLocal.getTodayEvents(), resource)
    }

    override fun getTomorrowEvents(): NotesResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWeekEvents(): CalendarResult {
        return CalendarResult(calendarLocal.getCurrentWeek(), resource)
    }
}
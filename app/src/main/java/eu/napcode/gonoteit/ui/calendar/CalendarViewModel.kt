package eu.napcode.gonoteit.ui.calendar

import android.arch.lifecycle.ViewModel
import eu.napcode.gonoteit.data.calendar.CalendarResult
import eu.napcode.gonoteit.repository.calendar.CalendarRepository
import javax.inject.Inject

class CalendarViewModel @Inject
constructor(private val calendarRepository: CalendarRepository) : ViewModel() {

    fun getTodayEvents(): CalendarResult {
        return calendarRepository.getTodayEvents()
    }
}

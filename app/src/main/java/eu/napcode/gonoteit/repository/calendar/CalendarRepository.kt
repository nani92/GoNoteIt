package eu.napcode.gonoteit.repository.calendar

import eu.napcode.gonoteit.data.calendar.CalendarResult
import eu.napcode.gonoteit.data.notes.results.NotesResult

interface CalendarRepository {
    fun getTodayEvents(): CalendarResult

    fun getTomorrowEvents(): NotesResult
}
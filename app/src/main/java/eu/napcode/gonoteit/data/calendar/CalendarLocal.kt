package eu.napcode.gonoteit.data.calendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import eu.napcode.gonoteit.dao.note.NoteDao
import eu.napcode.gonoteit.model.note.NoteModel
import java.util.*
import javax.inject.Inject

class CalendarLocal @Inject
constructor(private val noteDao: NoteDao) {

    fun getTodayEvents(): LiveData<List<NoteModel>> {
        val startCalendar = Calendar.getInstance()
        setBeginningOfDay(startCalendar)

        val endCalendar = Calendar.getInstance()
        setEndOfDay(endCalendar)

        return getEventsForDates(startCalendar, endCalendar)
    }

    private fun getEventsForDates(start: Calendar, end: Calendar): LiveData<List<NoteModel>> {
        return Transformations.map(noteDao.getBetweenDates(start.timeInMillis, end.timeInMillis)) {
            val models = mutableListOf<NoteModel>()

            it.forEach { noteEntity -> models.add(NoteModel(noteEntity))}

            models
        }
    }

    fun getCurrentWeek(): LiveData<List<NoteModel>> {
        val startCalendar = Calendar.getInstance()
        setBeginningOfDay(startCalendar)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.DAY_OF_MONTH, 7)
        setEndOfDay(endCalendar)

        return getEventsForDates(startCalendar, endCalendar)
    }

    private fun setBeginningOfDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
    }

    private fun setEndOfDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
    }
}
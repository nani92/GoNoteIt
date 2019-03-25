package eu.napcode.gonoteit.data.calendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import eu.napcode.gonoteit.dao.note.NoteDao
import eu.napcode.gonoteit.dao.note.NoteDaoManipulator
import eu.napcode.gonoteit.model.note.NoteModel
import java.util.*
import javax.inject.Inject

class CalendarLocal @Inject
constructor(private val noteDao: NoteDao) {

    fun getTodayEvents(): LiveData<List<NoteModel>> {
        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.HOUR_OF_DAY, 0)
        startCalendar.set(Calendar.MINUTE, 0)
        startCalendar.set(Calendar.SECOND, 0)

        val endCalendar = Calendar.getInstance()
        endCalendar.set(Calendar.HOUR_OF_DAY, 23)
        endCalendar.set(Calendar.MINUTE, 59)
        endCalendar.set(Calendar.SECOND, 59)

        return Transformations.map(
                noteDao.getBetweenDates(
                        startCalendar.timeInMillis / 1000,
                        endCalendar.timeInMillis / 1000)
        ) {
            val models = mutableListOf<NoteModel>()

            it.forEach { noteEntity -> models.add(NoteModel(noteEntity))}

            models
        }
    }
}
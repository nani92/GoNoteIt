package eu.napcode.gonoteit.data.calendar

import android.arch.lifecycle.LiveData
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.repository.Resource

class CalendarResult(notes: LiveData<List<NoteModel>>, resource: LiveData<Resource<*>>) {

    var notes: LiveData<List<NoteModel>>
        internal set

    var resource: LiveData<Resource<*>>
        internal set

    init {
        this.notes = notes
        this.resource = resource
    }
}

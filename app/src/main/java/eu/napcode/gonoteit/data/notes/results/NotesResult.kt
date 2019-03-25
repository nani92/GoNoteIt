package eu.napcode.gonoteit.data.notes.results

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.repository.Resource

class NotesResult(notes: LiveData<PagedList<NoteModel>>, resource: LiveData<Resource<*>>) {

    var notes: LiveData<PagedList<NoteModel>>
        internal set

    var resource: LiveData<Resource<*>>
        internal set

    init {
        this.notes = notes
        this.resource = resource
    }
}

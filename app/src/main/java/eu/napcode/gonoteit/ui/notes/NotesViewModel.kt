package eu.napcode.gonoteit.ui.notes

import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import eu.napcode.gonoteit.data.notes.results.DeletedResult
import eu.napcode.gonoteit.data.notes.results.NotesResult
import eu.napcode.gonoteit.repository.notes.NotesRepository

class NotesViewModel @Inject
constructor(val notesRepository: NotesRepository) : ViewModel() {

    val notes: NotesResult
        get() = notesRepository.getNotes()

    val favoriteNotes: NotesResult
        get() = notesRepository.getFavoriteNotes()


    fun deleteNote(id: Long?): DeletedResult {
        return notesRepository.deleteNote(id)
    }
}

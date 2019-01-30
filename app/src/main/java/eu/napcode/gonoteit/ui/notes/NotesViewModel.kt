package eu.napcode.gonoteit.ui.notes

import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import eu.napcode.gonoteit.data.results.DeletedResult
import eu.napcode.gonoteit.data.results.NotesResult
import eu.napcode.gonoteit.repository.notes.NotesRepository

class NotesViewModel @Inject
constructor(val notesRepository: NotesRepository) : ViewModel() {

    val notes: NotesResult
        get() = notesRepository.notes

    val favoriteNotes: NotesResult
        get() = notesRepository.favoriteNotes


    fun deleteNote(id: Long?): DeletedResult {
        return notesRepository.deleteNote(id)
    }
}

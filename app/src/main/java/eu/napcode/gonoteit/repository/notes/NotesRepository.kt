package eu.napcode.gonoteit.repository.notes

import android.arch.lifecycle.LiveData

import eu.napcode.gonoteit.data.notes.results.DeletedResult
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.data.notes.results.NoteResult
import eu.napcode.gonoteit.data.notes.results.NotesResult
import eu.napcode.gonoteit.repository.Resource

interface NotesRepository {

    fun getNotes(): NotesResult

    fun getFavoriteNotes(): NotesResult

    fun createNote(noteModel: NoteModel): LiveData<Resource<*>>

    fun deleteNote(id: Long?): DeletedResult

    fun getNote(id: Long?): NoteResult

    fun updateNote(noteModel: NoteModel): LiveData<Resource<*>>

    fun updateFavorites(id: Long): LiveData<Resource<*>>

    fun isNoteFavorite(id: Long?): LiveData<Boolean>
}

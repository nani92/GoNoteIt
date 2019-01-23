package eu.napcode.gonoteit.repository.notes;

import android.arch.lifecycle.LiveData;

import eu.napcode.gonoteit.data.results.DeletedResult;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.data.results.NoteResult;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.Resource;

public interface NotesRepository {

    NotesResult getNotes();

    LiveData<Resource> createNote(NoteModel noteModel);

    DeletedResult deleteNote(Long id);

    NoteResult getNote(Long id);

    LiveData<Resource> updateNote(NoteModel noteModel);

    LiveData<Resource> updateFavorites(Long id);

    NotesResult getFavoriteNotes();

    LiveData<Boolean> isNoteFavorite(Long id);
}

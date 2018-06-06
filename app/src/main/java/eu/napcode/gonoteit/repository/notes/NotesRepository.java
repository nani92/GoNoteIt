package eu.napcode.gonoteit.repository.notes;

import android.arch.lifecycle.LiveData;

import com.apollographql.apollo.api.Response;

import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.data.results.NoteResult;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import io.reactivex.Observable;

public interface NotesRepository {

    NotesResult getNotes();

    LiveData<Resource> createNote(NoteModel noteModel);

    Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id);

    NoteResult getNote(Long id);
}

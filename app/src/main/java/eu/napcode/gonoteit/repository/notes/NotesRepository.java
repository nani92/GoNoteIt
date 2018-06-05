package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.api.Response;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.model.note.NotesResult;
import io.reactivex.Observable;

public interface NotesRepository {

    NotesResult getNotes();

    Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel);

    Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id);

    Observable<NoteModel> getNote(Long id);
}

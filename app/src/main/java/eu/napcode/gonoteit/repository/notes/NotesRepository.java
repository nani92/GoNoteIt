package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.api.Response;

import java.util.List;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface NotesRepository {

    Flowable<List<NoteModel>> getNotes();

    Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel);
}

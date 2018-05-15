package eu.napcode.gonoteit.repository.notes;

import java.util.List;

import eu.napcode.gonoteit.model.note.NoteModel;
import io.reactivex.Flowable;

public interface NotesRepository {

    Flowable<List<NoteModel>> getNotes();
}

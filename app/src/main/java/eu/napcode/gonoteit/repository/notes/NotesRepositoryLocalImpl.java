package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.api.Response;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.dao.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class NotesRepositoryLocalImpl implements NotesRepository {

    private NoteDao noteDao;

    @Inject
    public NotesRepositoryLocalImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public Flowable<List<NoteModel>> getNotes() {
        return noteDao.getAllNoteEntities()
                .map(noteEntities -> {
                    List<NoteModel> noteModels = new ArrayList<>();

                    for (NoteEntity noteEntity : noteEntities) {
                        noteModels.add(new NoteModel(noteEntity));
                    }

                    return noteModels;
                });
    }

    @Override
    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {
        return Observable.error(new Throwable("Saving note offline is not implemented yet."));
    }
}

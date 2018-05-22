package eu.napcode.gonoteit.repository.notes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.dao.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;
import io.reactivex.Flowable;

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
}

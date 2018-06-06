package eu.napcode.gonoteit.repository.notes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import javax.inject.Inject;

import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.dao.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;

public class NotesLocal {
    private static final int PAGE_SIZE = 20;

    private NoteDao noteDao;

    @Inject
    public NotesLocal(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public LiveData<PagedList<NoteModel>> getNotes() {
        return new LivePagedListBuilder(noteDao.getAllNoteEntities()
                .map(input -> new NoteModel(input)),
                PAGE_SIZE)
                .build();
    }

    public LiveData<NoteModel> getNote(Long id) {
        LiveData<NoteEntity> noteEntityLiveData = noteDao.getNoteById(id);

        return Transformations.map(noteEntityLiveData, NoteModel::new);
    }
}

package eu.napcode.gonoteit.data.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.GetNotesQuery.AllEntity;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.dao.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;
import io.reactivex.Observable;
import timber.log.Timber;

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

    @SuppressLint("CheckResult")
    public void saveEntities(List<AllEntity> entities) {
        Observable.just(entities)
                .flatMapIterable(allEntities -> allEntities)
                .filter(allEntity -> allEntity.type() != Type.NONE)
                .map(allEntity -> (NoteModel) ((Note) allEntity.data()).parseNote(allEntity))
                .map(NoteEntity::new)
                .doOnEach(it -> {
                    if (it.getValue() != null) noteDao.insertNote(it.getValue());
                })
                .subscribe();
    }

    public void saveEntity(NoteModel noteModel) {
        noteDao.insertNote(new NoteEntity(noteModel));
    }

    public void deleteNote(Long id) {
        noteDao.removeNote(id);
    }
}

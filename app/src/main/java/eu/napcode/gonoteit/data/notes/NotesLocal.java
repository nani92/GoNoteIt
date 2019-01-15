package eu.napcode.gonoteit.data.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.GetChangelogMutation.Changelog;
import eu.napcode.gonoteit.GetNotesQuery.AllEntity;
import eu.napcode.gonoteit.api.ApiEntity;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.dao.note.NoteDaoManipulator;
import eu.napcode.gonoteit.dao.note.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;
import io.reactivex.Observable;

public class NotesLocal {
    private static final int PAGE_SIZE = 20;

    private NoteDaoManipulator noteDao;

    @Inject
    public NotesLocal(NoteDaoManipulator noteDaoManipulator) {
        this.noteDao = noteDaoManipulator;
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
                .map(allEntity -> (NoteModel) ((Note) allEntity.data()).parseNote(new ApiEntity(allEntity)))
                .map(NoteEntity::new)
                .filter(noteEntity -> noteEntity != null)
                .doOnEach(it ->  noteDao.insertNote(it.getValue()))
                .subscribe();
    }

    public void saveEntity(NoteModel noteModel) {
        noteDao.insertNote(new NoteEntity(noteModel));
    }

    public void deleteNote(Long id) {
        noteDao.deleteNote(id);
    }

    public void deleteAllNotes() {
        noteDao.deleteAll();
    }

    @SuppressLint("CheckResult")
    public void saveChangelog(Changelog changelog) {
        getNoteModelsToSaveObservable(changelog)
                .subscribe(noteModel -> saveEntity(noteModel));

        Observable.just(changelog.deleted())
                .flatMapIterable(deleteds -> deleteds)
                .map(deleted -> deleted.toString())
                .subscribe(deleted ->noteDao.deleteNoteByUuid(deleted));
    }

    private Observable<NoteModel> getNoteModelsToSaveObservable(Changelog changelog) {
        return getCreatedsObservable(changelog)
                .mergeWith(getUpdatedsObservable(changelog));
    }

    private Observable<NoteModel> getUpdatedsObservable(Changelog changelog) {
        return Observable.just(changelog.updated())
                .flatMapIterable(updateds -> updateds)
                .map(updated -> (NoteModel) ((Note) updated.data()).parseNote(new ApiEntity(updated)));
    }

    private Observable<NoteModel> getCreatedsObservable(Changelog changelog) {
        return Observable.just(changelog.created())
                .flatMapIterable(createds -> createds)
                .map(created -> (NoteModel) ((Note) created.data()).parseNote(new ApiEntity(created)));
    }
}

package eu.napcode.gonoteit.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import javax.inject.Inject;

import timber.log.Timber;

public class NoteDaoManipulator {

    private NoteDao noteDao;

    @Inject
    public NoteDaoManipulator(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public DataSource.Factory<Integer, NoteEntity> getAllNoteEntities() {
        return noteDao.getAllNoteEntities();
    }

    public void insertNote(NoteEntity noteEntity) {

        try {
            noteDao.insertNote(noteEntity);
            Timber.d("insert note completed %s", noteEntity.getId());
        } catch (Exception e) {
            Timber.d("insert note exception %s", e.getLocalizedMessage());
        }
    }

    public void removeAll() {

        try {
            noteDao.removeAll();
            Timber.d("remove all completed");
        } catch (Exception e) {
            Timber.d("remove all exception %s", e.getLocalizedMessage());
        }
    }

    public void removeNote(Long id) {

        try {
            noteDao.removeNote(id);
            Timber.d("remove note completed %s", id.toString());
        } catch (Exception e) {
            Timber.d("remove note exception %s", e.getLocalizedMessage());
        }
    }

    public void removeNoteByUuid(String id) {

        try {
            noteDao.removeNoteByUuid(id);
            Timber.d("remove note by uuid completed %s", id);
        } catch (Exception e) {
            Timber.d("remove note by uuid exception %s", e.getLocalizedMessage());
        }
    }

    public LiveData<NoteEntity> getNoteById(Long id) {
        return noteDao.getNoteById(id);
    }
}

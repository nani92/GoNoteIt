package eu.napcode.gonoteit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;

import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    Flowable<NoteEntity> getAllNoteEntities();
}

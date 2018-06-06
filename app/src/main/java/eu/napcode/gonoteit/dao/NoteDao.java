package eu.napcode.gonoteit.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import static eu.napcode.gonoteit.dao.NoteEntity.COLUMN_ID;
import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    DataSource.Factory<Integer, NoteEntity> getAllNoteEntities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);

    @Query("DELETE FROM " + TABLE_NAME)
    void removeAll();

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    void removeNote(Long id);

    @Query("SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_ID + " = :id")
    LiveData<NoteEntity> getNoteById(Long id);
}

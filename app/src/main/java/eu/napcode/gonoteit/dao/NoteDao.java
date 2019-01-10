package eu.napcode.gonoteit.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + NoteEntity.TABLE_NAME + " ORDER BY " + NoteEntity.COLUMN_UPDATED_AT + " DESC")
    DataSource.Factory<Integer, NoteEntity> getAllNoteEntities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);

    @Query("DELETE FROM " + NoteEntity.TABLE_NAME)
    void deleteAll();

    @Query("DELETE FROM " + NoteEntity.TABLE_NAME + " WHERE " + NoteEntity.COLUMN_ID + " = :id")
    void deleteNote(Long id);

    @Query("DELETE FROM " + NoteEntity.TABLE_NAME + " WHERE " + NoteEntity.COLUMN_UUID + " = :id")
    void deleteNoteByUuid(String id);

    @Query("SELECT * FROM " + NoteEntity.TABLE_NAME +" WHERE " + NoteEntity.COLUMN_ID + " = :id")
    LiveData<NoteEntity> getNoteById(Long id);
}

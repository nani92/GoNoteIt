package eu.napcode.gonoteit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    Flowable<List<NoteEntity>> getAllNoteEntities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);
}

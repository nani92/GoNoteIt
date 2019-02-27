package eu.napcode.gonoteit.dao.note

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface NoteDao {

    @get:Query("SELECT * FROM " + NoteEntity.TABLE_NAME + " ORDER BY " + NoteEntity.COLUMN_UPDATED_AT + " DESC")
    val allNoteEntities: DataSource.Factory<Int, NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteEntity: NoteEntity)

    @Query("DELETE FROM " + NoteEntity.TABLE_NAME)
    fun deleteAll()

    @Query("DELETE FROM " + NoteEntity.TABLE_NAME + " WHERE " + NoteEntity.COLUMN_ID + " = :id")
    fun deleteNote(id: Long?)

    @Query("SELECT * FROM " + NoteEntity.TABLE_NAME + " WHERE " + NoteEntity.COLUMN_ID + " = :id")
    fun getNoteById(id: Long?): LiveData<NoteEntity>

    @Query("SELECT * FROM " + NoteEntity.TABLE_NAME + " WHERE id IN (:ids)" + " ORDER BY " + NoteEntity.COLUMN_UPDATED_AT + " DESC")
    fun getFavoriteNoteEntities(ids: List<Long>): DataSource.Factory<Int, NoteEntity>

}

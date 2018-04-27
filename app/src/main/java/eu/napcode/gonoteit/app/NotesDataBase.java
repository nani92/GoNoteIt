package eu.napcode.gonoteit.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.dao.NoteEntity;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NotesDataBase extends RoomDatabase {

    public static final String NOTES_DATA_BASE_NAME = "gonoteit.db";

    public abstract NoteDao noteDao();
}

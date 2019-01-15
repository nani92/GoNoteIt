package eu.napcode.gonoteit.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import eu.napcode.gonoteit.dao.note.NoteDao;
import eu.napcode.gonoteit.dao.note.NoteEntity;
import eu.napcode.gonoteit.dao.user.UserDao;
import eu.napcode.gonoteit.dao.user.UserEntity;
import eu.napcode.gonoteit.utils.ReadAccessConverter;
import eu.napcode.gonoteit.utils.WriteAccessConverter;

@Database(entities = {NoteEntity.class, UserEntity.class}, version = 1)
@TypeConverters({
        ReadAccessConverter.class,
        WriteAccessConverter.class})
public abstract class NotesDataBase extends RoomDatabase {

    public static final String NOTES_DATA_BASE_NAME = "gonoteit.db";

    public abstract NoteDao noteDao();

    public abstract UserDao userDao();
}

package eu.napcode.gonoteit.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.napcode.gonoteit.app.NotesDataBase;
import eu.napcode.gonoteit.dao.note.NoteDao;
import eu.napcode.gonoteit.dao.user.UserDao;

@Module
public class StorageModule {

    @Singleton
    @Provides
    NotesDataBase noteDataBase(Context context) {
        return Room
                .databaseBuilder(context, NotesDataBase.class, NotesDataBase.Companion.getNOTES_DATA_BASE_NAME())
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    NoteDao noteDao(Context context) {
        return noteDataBase(context).noteDao();
    }

    @Singleton
    @Provides
    UserDao userDao(Context context) {
        return noteDataBase(context).userDao();
    }
}

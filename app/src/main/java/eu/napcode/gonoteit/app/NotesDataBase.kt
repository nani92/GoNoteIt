package eu.napcode.gonoteit.app

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

import eu.napcode.gonoteit.dao.note.NoteDao
import eu.napcode.gonoteit.dao.note.NoteEntity
import eu.napcode.gonoteit.dao.user.UserDao
import eu.napcode.gonoteit.dao.user.UserEntity
import eu.napcode.gonoteit.utils.AccessConverter

@Database(
        entities = arrayOf(NoteEntity::class, UserEntity::class),
        version = 1)
@TypeConverters(AccessConverter::class)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun userDao(): UserDao

    companion object {

        val NOTES_DATA_BASE_NAME = "gonoteit.db"
    }
}

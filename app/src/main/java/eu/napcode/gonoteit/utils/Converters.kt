package eu.napcode.gonoteit.utils

import android.arch.persistence.room.TypeConverter
import eu.napcode.gonoteit.type.ReadAccess
import eu.napcode.gonoteit.type.WriteAccess

class ReadAccessConverter {

    @TypeConverter
    fun toReadPerms(ordinal: Int): ReadAccess {
        return ReadAccess.values()[ordinal]
    }

    @TypeConverter
    fun toOrdinal(readAccess: ReadAccess): Int? {
        return readAccess.ordinal
    }
}

class WriteAccessConverter {

    @TypeConverter
    fun toWritePerms(ordinal: Int): WriteAccess {
        return WriteAccess.values()[ordinal]
    }

    @TypeConverter
    fun toOrdinal(writePerms: WriteAccess): Int? {
        return writePerms.ordinal
    }
}


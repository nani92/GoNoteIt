package eu.napcode.gonoteit.utils

import android.arch.persistence.room.TypeConverter
import eu.napcode.gonoteit.type.Access

class AccessConverter {

    @TypeConverter
    fun toReadPerms(ordinal: Int): Access {
        return Access.values()[ordinal]
    }

    @TypeConverter
    fun toOrdinal(readAccess: Access): Int? {
        return readAccess.ordinal
    }
}

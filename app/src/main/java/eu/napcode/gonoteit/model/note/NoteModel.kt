package eu.napcode.gonoteit.model.note

import android.arch.persistence.room.TypeConverter
import eu.napcode.gonoteit.dao.NoteEntity
import eu.napcode.gonoteit.type.ReadAccess
import eu.napcode.gonoteit.type.WriteAccess

open class NoteModel (){
    var uuid: String? = null
    var title: String? = null
    var content: String? = null
    var id: Long? = null
    var imageBase64: String? = null
    var updatedAt: Long? = null
    var readAccess = ReadAccess.PRIVATE
    var writeAccess = WriteAccess.ONLY_OWNER


    constructor(noteEntity: NoteEntity): this() {
        uuid = noteEntity.uuid
        title = noteEntity.title
        content = noteEntity.content
        id = noteEntity.id
        imageBase64 = noteEntity.imageBase64
        updatedAt = noteEntity.updatedAt
        readAccess = noteEntity.readAccess
        writeAccess = noteEntity.writeAccess
    }

    override fun equals(obj: Any?): Boolean {
        return obj is NoteModel &&
                uuid == obj.uuid &&
                id == obj.id &&
                areStringsEqual(title, obj.title) &&
                areStringsEqual(content, obj.content) &&
                areStringsEqual(imageBase64, obj.imageBase64)
    }

    private fun areStringsEqual(s: String?, s2: String?): Boolean {
        return s == null && s2 == null || s == s2
    }

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

}

package eu.napcode.gonoteit.model.note

import android.arch.persistence.room.TypeConverter
import eu.napcode.gonoteit.dao.NoteEntity

open class NoteModel (){
    var uuid: String? = null
    var title: String? = null
    var content: String? = null
    var id: Long? = null
    var imageBase64: String? = null
    var updatedAt: Long? = null
    var readPerms: ReadPerms = ReadPerms.PRIVATE
    var writePerms: WritePerms = WritePerms.ONLY_OWNER


    constructor(noteEntity: NoteEntity): this() {
        uuid = noteEntity.uuid
        title = noteEntity.title
        content = noteEntity.content
        id = noteEntity.id
        imageBase64 = noteEntity.imageBase64
        updatedAt = noteEntity.updatedAt
        readPerms = noteEntity.readPerms
        writePerms = noteEntity.writePerms
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

    enum class ReadPerms {
        PUBLIC,
        PRIVATE,
        VIA_LINK
    }

    class ReadPermsConverter {

        @TypeConverter
        fun toReadPerms(ordinal: Int): NoteModel.ReadPerms {
            return NoteModel.ReadPerms.values()[ordinal]
        }

        @TypeConverter
        fun toOrdinal(readPerms: NoteModel.ReadPerms): Int? {
            return readPerms.ordinal
        }
    }

    enum class WritePerms {
        ONLY_OWNER,
        EVERYONE
    }

    class WritePermsConverter {

        @TypeConverter
        fun toWritePerms(ordinal: Int): NoteModel.WritePerms {
            return NoteModel.WritePerms.values()[ordinal]
        }

        @TypeConverter
        fun toOrdinal(writePerms: NoteModel.WritePerms): Int? {
            return writePerms.ordinal
        }
    }

}

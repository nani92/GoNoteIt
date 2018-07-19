package eu.napcode.gonoteit.dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import eu.napcode.gonoteit.dao.NoteEntity.Companion.TABLE_NAME

import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.type.ReadAccess
import eu.napcode.gonoteit.type.WriteAccess
import eu.napcode.gonoteit.utils.ReadAccessConverter
import eu.napcode.gonoteit.utils.WriteAccessConverter

@Entity(tableName = TABLE_NAME)
data class NoteEntity(

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = COLUMN_UUID)
        var uuid: String,

        @ColumnInfo(name = COLUMN_ID)
        var id: Long,

        @ColumnInfo(name = COLUMN_TITLE)
        var title: String?,

        @ColumnInfo(name = COLUMN_CONTENT)
        var content: String?,

        @ColumnInfo(name = COLUMN_IMAGE)
        var imageBase64: String?,

        @ColumnInfo(name = COLUMN_UPDATED_AT)
        var updatedAt: Long,

        @TypeConverters(ReadAccessConverter::class)
        @ColumnInfo(name = COLUMN_READ_PERMS)
        var readAccess : ReadAccess,

        @TypeConverters(WriteAccessConverter::class)
        @ColumnInfo(name = COLUMN_WRITE_PERMS)
        var writeAccess: WriteAccess
) {

    constructor(noteModel: NoteModel) :
            this(
                    uuid = noteModel.uuid!!,
                    id = noteModel.id!!,
                    updatedAt = noteModel.updatedAt!!,
                    title = noteModel.title,
                    content = noteModel.content,
                    imageBase64 = noteModel.imageBase64,
                    readAccess= noteModel.readAccess,
                    writeAccess= noteModel.writeAccess)


    companion object {

        const val TABLE_NAME = "notes"

        const val COLUMN_UUID = "UUID"
        const val COLUMN_ID = "ID"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_UPDATED_AT = "updated"
        const val COLUMN_READ_PERMS = "read"
        const val COLUMN_WRITE_PERMS = "write"
    }
}

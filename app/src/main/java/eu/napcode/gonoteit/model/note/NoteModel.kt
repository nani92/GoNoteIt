package eu.napcode.gonoteit.model.note

import eu.napcode.gonoteit.dao.note.NoteEntity
import eu.napcode.gonoteit.type.Access

open class NoteModel (){
    var uuid: String? = null
    var title: String? = null
    var content: String? = null
    var id: Long? = null
    var imageBase64: String? = null
    var updatedAt: Long? = null
    var readAccess = Access.INTERNAL
    var writeAccess = Access.INTERNAL

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
}

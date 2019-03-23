package eu.napcode.gonoteit.model.note

import eu.napcode.gonoteit.dao.note.NoteEntity
import eu.napcode.gonoteit.type.Access

open class NoteModel (){
    var title: String? = null
    var content: String? = null
    var id: Long? = null
    var imageBase64: String? = null
    var updatedAt: Long? = null
    var readAccess = Access.INTERNAL
    var writeAccess = Access.INTERNAL
    var date: Long? = null
    var hasAttachment = false

    constructor(noteEntity: NoteEntity): this() {
        title = noteEntity.title
        content = noteEntity.content
        id = noteEntity.id
        imageBase64 = noteEntity.imageBase64
        updatedAt = noteEntity.updatedAt
        readAccess = noteEntity.readAccess
        writeAccess = noteEntity.writeAccess
        date = noteEntity.date
        hasAttachment = noteEntity.hasAttachment
    }

    override fun equals(obj: Any?): Boolean {
        return obj is NoteModel &&
                id == obj.id &&
                areStringsEqual(title, obj.title) &&
                areStringsEqual(content, obj.content) &&
                areStringsEqual(imageBase64, obj.imageBase64) &&
                date == obj.date && hasAttachment == obj.hasAttachment
    }

    private fun areStringsEqual(s: String?, s2: String?): Boolean {
        return s == null && s2 == null || s == s2
    }
}

package eu.napcode.gonoteit.model.note

import eu.napcode.gonoteit.dao.NoteEntity

open class NoteModel {

    var uuid: String? = null
    var title: String? = null
    var content: String? = null
    var id: Long? = null
    var imageBase64: String? = null
    var updatedAt: Long? = null
    var readPerms: ReadPerms = ReadPerms.PRIVATE
    var writePerms: WritePerms = WritePerms.ONLY_OWNER

    //TODO check if could be removed
    constructor()

    constructor(noteEntity: NoteEntity) {
        this.uuid = noteEntity.uuid
        this.title = noteEntity.title
        this.content = noteEntity.content
        this.id = noteEntity.id
        this.imageBase64 = noteEntity.imageBase64
        this.updatedAt = noteEntity.updatedAt
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

    enum class WritePerms {
        ONLY_OWNER,
        EVERYONE
    }
}

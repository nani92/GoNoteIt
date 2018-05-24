package eu.napcode.gonoteit.model.note;

import eu.napcode.gonoteit.dao.NoteEntity;

public class NoteModel {

    public NoteModel() {
    }

    public NoteModel(NoteEntity noteEntity) {
        this.uuid = noteEntity.getUuid();
        this.title = noteEntity.getTitle();
        this.content = noteEntity.getContent();
        this.id = noteEntity.getId();
    }

    private String uuid;
    private String title;
    private String content;
    private Long id;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

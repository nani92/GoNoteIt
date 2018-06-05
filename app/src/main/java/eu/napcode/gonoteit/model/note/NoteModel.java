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
        this.imageBase64 = noteEntity.getImageBase64();
    }

    private String uuid;
    private String title;
    private String content;
    private Long id;
    private String imageBase64;

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

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NoteModel &&
                uuid.equals(((NoteModel) obj).uuid) &&
                id.equals(((NoteModel) obj).id) &&
                title.equals(((NoteModel) obj).title) &&
                content.equals(((NoteModel) obj).content) &&
                imageBase64.equals(((NoteModel) obj).imageBase64);
    }
}

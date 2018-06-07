package eu.napcode.gonoteit.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import javax.annotation.Nullable;

import eu.napcode.gonoteit.model.note.NoteModel;

import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class NoteEntity {

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_UUID = "UUID";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_UPDATED_AT = "updated";

    public NoteEntity() {
    }

    public NoteEntity(NoteModel noteModel) {
        this.setUuid(noteModel.getUuid());
        this.setTitle(noteModel.getTitle());
        this.setContent(noteModel.getContent());
        this.setId(noteModel.getId());
        this.setImageBase64(noteModel.getImageBase64());
        this.setUpdatedAt(noteModel.getUpdatedAt());
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = COLUMN_UUID)
    String uuid;

    @NonNull
    @ColumnInfo(name = COLUMN_ID)
    Long id;

    @Nullable
    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    @Nullable
    @ColumnInfo(name = COLUMN_CONTENT)
    private String content;

    @Nullable
    @ColumnInfo(name = COLUMN_IMAGE)
    private String imageBase64;

    @ColumnInfo(name = COLUMN_UPDATED_AT)
    private Long updatedAt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    public void setImageBase64(@Nullable String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package eu.napcode.gonoteit.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import javax.annotation.Nullable;

import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class NoteEntity {

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    String id;

    @Nullable
    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    @Nullable
    @ColumnInfo(name = COLUMN_CONTENT)
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}

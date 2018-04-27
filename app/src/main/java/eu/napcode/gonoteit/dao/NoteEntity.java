package eu.napcode.gonoteit.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static eu.napcode.gonoteit.dao.NoteEntity.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class NoteEntity {

    public static final String TABLE_NAME = "notes";

    @PrimaryKey
    int id;
}

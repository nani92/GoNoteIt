package eu.napcode.gonoteit.api;

import com.google.gson.Gson;

import eu.napcode.gonoteit.GetNoteByIdQuery.Entity;
import eu.napcode.gonoteit.UpdateNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.model.note.SimpleNoteModel;
import eu.napcode.gonoteit.type.Type;

import static eu.napcode.gonoteit.GetNotesQuery.*;
import static eu.napcode.gonoteit.type.Type.NONE;
import static eu.napcode.gonoteit.type.Type.NOTE;

public class Note {

    private String noteDataString;
    private String uuid;
    private Type type;
    private Long id;

    public Note(String noteDataString) {
        this.noteDataString = noteDataString;
    }

    public Note(NoteModel noteModel) {
        Gson gson = new Gson();
        this.noteDataString = gson.toJson(noteModel);
    }

    public String getNoteDataString() {
        return noteDataString;
    }

    public <T extends NoteModel> T parseNote(AllEntity allEntity) {
        this.id = allEntity.id();
        this.type = allEntity.type();
        this.uuid = allEntity.uuid().toString();

        return parseNote();
    }

    public <T extends NoteModel> T parseNote(Entity entity) {
        this.id = entity.id();
        this.type = entity.type();
        this.uuid = entity.uuid().toString();

        return parseNote();
    }

    public <T extends NoteModel> T parseNote(UpdateNoteMutation.Entity entity) {
        this.id = entity.id();
        this.type = entity.type();
        this.uuid = entity.uuid().toString();

        return parseNote();
    }

    private <T extends NoteModel> T parseNote() {

        if (type == NONE) {
            return null;
        }

        if (type == NOTE) {
            return (T) getNoteModel();
        }

        return (T) getNoteModel();
    }

    private SimpleNoteModel getNoteModel() {

        if (type == NONE) {
            return null;
        }

        Gson gson = new Gson();
        SimpleNoteModel noteModel = gson.fromJson(noteDataString, SimpleNoteModel.class);
        noteModel.setUuid(uuid);
        noteModel.setId(id);

        return noteModel;
    }
}

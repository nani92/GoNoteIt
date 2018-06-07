package eu.napcode.gonoteit.api;

import com.google.gson.Gson;

import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.model.note.SimpleNoteModel;
import static eu.napcode.gonoteit.type.Type.NONE;
import static eu.napcode.gonoteit.type.Type.NOTE;

public class Note {

    private String noteDataString;
    private ApiEntity apiEntity;

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

    public <T extends NoteModel> T parseNote(ApiEntity apiEntity) {
        this.apiEntity = apiEntity;

        return parseNote();
    }

    private <T extends NoteModel> T parseNote() {

        if (apiEntity.type == NONE) {
            return null;
        }

        if (apiEntity.type == NOTE) {
            return (T) getNoteModel();
        }

        return (T) getNoteModel();
    }

    private SimpleNoteModel getNoteModel() {
        Gson gson = new Gson();
        SimpleNoteModel noteModel = gson.fromJson(noteDataString, SimpleNoteModel.class);
        noteModel.setUuid(apiEntity.uuid);
        noteModel.setId(apiEntity.id);
        noteModel.setUpdatedAt(apiEntity.updatedAt);

        return noteModel;
    }
}

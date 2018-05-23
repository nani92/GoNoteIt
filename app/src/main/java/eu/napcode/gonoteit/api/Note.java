package eu.napcode.gonoteit.api;

import com.google.gson.Gson;

import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;

public class Note {

    private String noteString;

    public Note(String noteString) {
        this.noteString = noteString;
    }

    public Note(NoteModel noteModel) {
        Gson gson = new Gson();
        this.noteString = gson.toJson(noteModel);
    }

    public String getNoteString() {
        return noteString;
    }

    public <T> T parseNote(Type type, Object uuid) {
        return (T) getNoteModel(type, uuid.toString());
    }

    private NoteModel getNoteModel(Type type, String uuid) {

        if (type == Type.NONE) {
            return null;
        }

        Gson gson = new Gson();
        NoteModel noteModel = gson.fromJson(noteString, NoteModel.class);
        noteModel.setId(uuid);

        return noteModel;
    }
}

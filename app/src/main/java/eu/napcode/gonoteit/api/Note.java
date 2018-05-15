package eu.napcode.gonoteit.api;

import com.google.gson.Gson;

import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;

public class Note {

    private String noteString;

    public Note(String noteString) {
        this.noteString = noteString;
    }

    public String getNoteString() {
        return noteString;
    }

    public <T> T parseNote(Type type) {
        return (T) getNoteModel(type);
    }

    private NoteModel getNoteModel(Type type) {

        if (type == Type.NONE) {
            return null;
        }

        Gson gson = new Gson();

        return gson.fromJson(noteString, NoteModel.class);
    }
}

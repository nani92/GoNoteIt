package eu.napcode.gonoteit.data.results;

import android.arch.lifecycle.LiveData;

import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;

public class NoteResult {

    LiveData<NoteModel> note;
    LiveData<Resource> resource;

    public NoteResult(LiveData<NoteModel> note, LiveData<Resource> resource) {
        this.note = note;
        this.resource = resource;
    }

    public LiveData<NoteModel> getNote() {
        return note;
    }

    public LiveData<Resource> getResource() {
        return resource;
    }
}

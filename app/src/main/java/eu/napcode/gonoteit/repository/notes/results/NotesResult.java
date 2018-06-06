package eu.napcode.gonoteit.repository.notes.results;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;

public class NotesResult {

    LiveData<PagedList<NoteModel>> notes;
    LiveData<Resource> resource;

    public NotesResult(LiveData<PagedList<NoteModel>> notes, LiveData<Resource> resource) {
        this.notes = notes;
        this.resource = resource;
    }

    public LiveData<PagedList<NoteModel>> getNotes() {
        return notes;
    }

    public LiveData<Resource> getResource() {
        return resource;
    }
}

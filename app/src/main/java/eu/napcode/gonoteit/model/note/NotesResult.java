package eu.napcode.gonoteit.model.note;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import eu.napcode.gonoteit.repository.Resource;

public class NotesResult {

    LiveData<PagedList<NoteModel>> notes;
    LiveData<Resource> resourceLiveData;

    public NotesResult(LiveData<PagedList<NoteModel>> notes, LiveData<Resource> resourceLiveData) {
        this.notes = notes;
        this.resourceLiveData = resourceLiveData;
    }

    public LiveData<PagedList<NoteModel>> getNotes() {
        return notes;
    }

    public LiveData<Resource> getResource() {
        return resourceLiveData;
    }
}

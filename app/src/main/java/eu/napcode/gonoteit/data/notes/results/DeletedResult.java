package eu.napcode.gonoteit.data.notes.results;

import android.arch.lifecycle.LiveData;

import eu.napcode.gonoteit.repository.Resource;

public class DeletedResult {

    private Long id;
    private LiveData<Resource> resource;

    public DeletedResult(Long id, LiveData<Resource> resource) {
        this.id = id;
        this.resource = resource;
    }

    public Long getId() {
        return id;
    }

    public LiveData<Resource> getResource() {
        return resource;
    }
}

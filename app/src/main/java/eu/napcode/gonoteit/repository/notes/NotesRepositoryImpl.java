package eu.napcode.gonoteit.repository.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.api.ApiEntity;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.data.notes.NotesLocal;
import eu.napcode.gonoteit.data.notes.NotesRemote;
import eu.napcode.gonoteit.data.results.DeletedResult;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.data.results.NoteResult;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;

public class NotesRepositoryImpl implements NotesRepository {

    private final ErrorMessages errorMessages;
    private final NotesLocal notesLocal;
    private NotesRemote notesRemote;
    private NetworkHelper networkHelper;

    MutableLiveData<Resource> resource = new MutableLiveData<>();

    @Inject
    public NotesRepositoryImpl(NotesRemote notesRemote, NotesLocal notesLocal,
                               NetworkHelper networkHelper, ErrorMessages errorMessages) {
        this.notesRemote = notesRemote;
        this.notesLocal = notesLocal;
        this.networkHelper = networkHelper;
        this.errorMessages = errorMessages;
    }

    @Override
    public NotesResult getNotes() {

        if (networkHelper.isNetworkAvailable()) {
            updateNotesFromRemote();
        } else {
            resource.postValue(Resource.error(new Throwable(errorMessages.getOfflineMessage())));
        }

        return new NotesResult(notesLocal.getNotes(), resource);
    }

    private void updateNotesFromRemote() {

        if (notesRemote.shouldFetchChangelog()) {
            getNotesChangelog();
        } else {
            getNotesFromRemote();
        }
    }

    @SuppressLint("CheckResult")
    private void getNotesChangelog() {
        //TODO Clean it up
        notesRemote.getChangelog()
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
//                .filter(dataResponse -> dataResponse.hasErrors())
//                .filter(dataResponse -> dataResponse.data().changelog() != null)
//                .singleOrError()
//                .map(dataResponse -> dataResponse.data().changelog())
//                .doOnSuccess(it -> notesLocal.saveChangelog(it))
//                .doOnSuccess(it -> notesRemote.saveTimestamp(it.timestamp()))
                .subscribe(
                        dataResponse -> {

                            if (dataResponse.hasErrors()) {
                                resource.postValue(Resource.success(null));
                                //TODO auth error already displayed
                                //    resource.postValue(Resource.error(dataResponse.errors().get(0)));
                            } else {
                                resource.postValue(Resource.success(null));
                            }

                            if (dataResponse.data().changelog() != null) {
                                notesLocal.saveChangelog(dataResponse.data().changelog());
                                notesRemote.saveTimestamp(dataResponse.data().changelog().timestamp());
                            }
                        },
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @SuppressLint("CheckResult")
    private void getNotesFromRemote() {
        notesRemote.getNotes()
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .subscribe(
                        data -> {
                            resource.postValue(Resource.success(null));
                            notesLocal.saveEntities(data);
                        },
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @Override
    public LiveData<Resource> createNote(NoteModel noteModel) {

        if (networkHelper.isNetworkAvailable()) {
            createNoteOnRemote(noteModel);
        } else {
            resource.postValue(Resource.error(new Throwable(errorMessages.getCreatingNoteNotImplementedOfflineMessage())));
        }

        return resource;
    }

    @SuppressLint("CheckResult")
    private void createNoteOnRemote(NoteModel noteModel) {
        notesRemote.createNote(noteModel)
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .filter(response -> response.data() != null)
                .filter(response -> response.data().createEntity() != null)
                .filter(response -> response.data().createEntity().ok())
                .singleOrError()
                .doOnSuccess(it -> getNotesChangelog())
                .map(dataResponse -> dataResponse.data().createEntity().entity())
                .map(entity -> (NoteModel) ((Note) entity.data()).parseNote(new ApiEntity(entity)))
                .doOnSuccess(notesLocal::saveEntity)
                .subscribe(
                        response -> resource.postValue(Resource.success(null)),
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @Override
    public DeletedResult deleteNote(Long id) {
        MutableLiveData<Resource> resource = new MutableLiveData<>();

        if (networkHelper.isNetworkAvailable()) {
            deleteNoteOnRemote(id, resource);
        } else {
            resource.postValue(Resource.error(new Throwable(errorMessages.getDeletingNoteNotImplementedOfflineMessage())));
        }

        return new DeletedResult(id, resource);
    }

    @SuppressLint("CheckResult")
    private void deleteNoteOnRemote(Long id, MutableLiveData<Resource> resource) {
        notesRemote.deleteNote(id)
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .filter(response -> response.data() != null)
                .filter(response -> response.data().deleteEntity() != null)
                .filter(response -> response.data().deleteEntity().deleted())
                .singleOrError()
                .doOnSuccess(it -> getNotesChangelog())
                .subscribe(
                        response -> resource.postValue(Resource.success(null)),
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @Override
    public NoteResult getNote(Long id) {

        if (networkHelper.isNetworkAvailable()) {
            getNoteFromRemote(id);
        } else {
            resource.postValue(Resource.error(new Throwable(errorMessages.getOfflineMessage())));
        }

        return new NoteResult(notesLocal.getNote(id), resource);
    }

    @SuppressLint("CheckResult")
    private void getNoteFromRemote(Long id) {
        notesRemote.getNote(id)
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .subscribe(
                        noteModel -> {
                            notesLocal.saveEntity(noteModel);
                            resource.postValue(Resource.success(null));
                        },
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @Override
    public LiveData<Resource> updateNote(NoteModel noteModel) {

        if (networkHelper.isNetworkAvailable()) {
            updateNoteOnRemote(noteModel);
        } else {
            resource.postValue(Resource.error(errorMessages.getUpdatingNoteNotImplementedOfflineMessage()));
        }

        return resource;
    }


    @SuppressLint("CheckResult")
    private void updateNoteOnRemote(NoteModel noteModel) {
        notesRemote.updateNote(noteModel)
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .filter(response -> response.data() != null)
                .filter(response -> response.data().updateEntity() != null)
                .filter(response -> response.data().updateEntity().ok())
                .singleOrError()
                .map(dataResponse -> dataResponse.data().updateEntity().entity())
                .map(entity -> (NoteModel) ((Note) entity.data()).parseNote(new ApiEntity(entity)))
                .doOnSuccess(notesLocal::saveEntity)
                .doOnSuccess(it -> getNotesChangelog())
                .subscribe(
                        response -> resource.postValue(Resource.success(null)),
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @Override
    public LiveData<Resource> updateFavorites(Long id) {
        List<Long> list = new ArrayList<>();
        list.add(id);
        if (networkHelper.isNetworkAvailable()) {
            notesRemote.updateFavorites(list).subscribe();
        } else {
            resource.postValue(Resource.error(errorMessages.getUpdatingNoteNotImplementedOfflineMessage()));
        }

        return resource;
    }
}

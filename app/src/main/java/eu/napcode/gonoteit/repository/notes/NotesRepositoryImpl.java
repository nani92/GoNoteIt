package eu.napcode.gonoteit.repository.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.apollographql.apollo.api.Response;

import javax.inject.Inject;

import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.data.notes.NotesLocal;
import eu.napcode.gonoteit.data.notes.NotesRemote;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.data.results.NoteResult;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.rx.RxSchedulers;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;

public class NotesRepositoryImpl implements NotesRepository {

    private final ErrorMessages errorMessages;
    private final NotesLocal notesLocal;
    private RxSchedulers rxSchedulers;
    private NotesRemote notesRemote;
    private NetworkHelper networkHelper;

    MutableLiveData<Resource> resource = new MutableLiveData<>();

    @Inject
    public NotesRepositoryImpl(NotesRemote notesRemote, NotesLocal notesLocal,
                               NetworkHelper networkHelper, RxSchedulers rxSchedulers,
                               ErrorMessages errorMessages) {
        this.notesRemote = notesRemote;
        this.notesLocal = notesLocal;
        this.networkHelper = networkHelper;
        this.rxSchedulers = rxSchedulers;
        this.errorMessages = errorMessages;
    }

    @Override
    public NotesResult getNotes() {

        if (networkHelper.isNetworkAvailable()) {
            updateLocalNotesFromRemote();
        } else {
            resource.postValue(Resource.error(new Throwable(errorMessages.getOfflineMessage())));
        }

        return new NotesResult(notesLocal.getNotes(), resource);
    }

    @SuppressLint("CheckResult")
    private void updateLocalNotesFromRemote() {
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
                .doOnSuccess(it -> updateLocalNotesFromRemote())
                .subscribe(
                        response -> resource.postValue(Resource.success(null)),
                        error -> resource.postValue(Resource.error(error))
                );
    }

    @Override
    public Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRemote.deleteNote(id);
        } else {
            return Observable.error(new Throwable(errorMessages.getDeletingNoteNotImplementedOfflineMessage()));
        }
    }


    @Override
    public NoteResult getNote(Long id) {

        if (networkHelper.isNetworkAvailable()) {
            updateLocalNoteFromRemote(id);
        } else {
            resource.postValue(Resource.error(new Throwable(errorMessages.getOfflineMessage())));
        }

        return new NoteResult(notesLocal.getNote(id), resource);
    }

    @SuppressLint("CheckResult")
    private void updateLocalNoteFromRemote(Long id) {
        notesRemote.getNote(id)
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .subscribeOn(rxSchedulers.io())
                .subscribe(
                        noteModel -> {
                            notesLocal.saveEntity(noteModel);
                            resource.postValue(Resource.success(null));
                        },
                        error -> resource.postValue(Resource.error(error))
                );
    }
}

package eu.napcode.gonoteit.repository.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;

import com.apollographql.apollo.api.Response;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.notes.results.NoteResult;
import eu.napcode.gonoteit.repository.notes.results.NotesResult;
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
            updateNotesFromRemote();
        } else  {
            resource.postValue(Resource.error(new Throwable(errorMessages.getOfflineMessage())));
        }

        return new NotesResult(notesLocal.getNotes(), resource);
    }

    @SuppressLint("CheckResult")
    private void updateNotesFromRemote() {
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
    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRemote.createNote(noteModel);
        } else {
            return Observable.error(new Throwable(errorMessages.getCreatingNoteNotImplementedOfflineMessage()));
        }
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
            updateNoteFromRemote(id);
        } else  {
            resource.postValue(Resource.error(new Throwable(errorMessages.getOfflineMessage())));
        }

        return new NoteResult(notesLocal.getNote(id), resource);
    }

    @SuppressLint("CheckResult")
    private void updateNoteFromRemote(Long id) {
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

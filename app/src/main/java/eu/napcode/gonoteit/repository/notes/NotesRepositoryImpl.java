package eu.napcode.gonoteit.repository.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;

import com.apollographql.apollo.api.Response;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.model.note.NoteResult;
import eu.napcode.gonoteit.model.note.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.rx.RxSchedulers;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;

public class NotesRepositoryImpl implements NotesRepository {

    private final ErrorMessages errorMessages;
    private final NotesLocal notesLocal;
    private RxSchedulers rxSchedulers;
    private NotesRepositoryRemoteImpl notesRepositoryRemote;
    private NetworkHelper networkHelper;

    MutableLiveData<Resource> resource = new MutableLiveData<>();

    @Inject
    public NotesRepositoryImpl(NotesRepositoryRemoteImpl notesRepositoryRemote, NotesLocal notesLocal,
                               NetworkHelper networkHelper, RxSchedulers rxSchedulers,
                               ErrorMessages errorMessages) {
        this.notesRepositoryRemote = notesRepositoryRemote;
        this.notesLocal = notesLocal;
        this.networkHelper = networkHelper;
        this.rxSchedulers = rxSchedulers;
        this.errorMessages = errorMessages;
    }

    @SuppressLint("CheckResult")
    @Override
    public NotesResult getNotes() {
        notesRepositoryRemote.getNotes()
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .subscribeOn(rxSchedulers.io())
                .subscribe(
                        noteModels -> resource.postValue(Resource.success(null)),
                        error -> resource.postValue(Resource.error(error))
                );

        /*if (networkHelper.isNetworkAvailable()) {
            return this.notesRepositoryRemote.getNotes();
        } else {
            return this.notesRepositoryLocal.getNotes();
        }*/

        return new NotesResult(notesLocal.getNotes(), resource);
    }

    @Override
    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRepositoryRemote.createNote(noteModel);
        } else {
            return Observable.error(new Throwable(errorMessages.getCreatingNoteNotImplementedOfflineMessage()));
        }
    }

    @Override
    public Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRepositoryRemote.deleteNote(id);
        } else {
            return Observable.error(new Throwable(errorMessages.getDeletingNoteNotImplementedOfflineMessage()));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public NoteResult getNote(Long id) {
        notesRepositoryRemote.getNote(id)
                .doOnSubscribe(it -> resource.postValue(Resource.loading(null)))
                .subscribeOn(rxSchedulers.io())
                .subscribe(
                        noteModel -> resource.postValue(Resource.success(null)),
                        error -> resource.postValue(Resource.error(error))
                );

        return new NoteResult(notesLocal.getNote(id), resource);
    }
}

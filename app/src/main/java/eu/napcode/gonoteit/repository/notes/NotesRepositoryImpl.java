package eu.napcode.gonoteit.repository.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.apollographql.apollo.api.Response;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.model.note.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.rx.RxSchedulers;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;

public class NotesRepositoryImpl implements NotesRepository {

    private final ErrorMessages errorMessages;
    private RxSchedulers rxSchedulers;
    private NotesRepositoryRemoteImpl notesRepositoryRemote;
    private NetworkHelper networkHelper;
    private NoteDao noteDao;

    private static final int PAGE_SIZE = 20;

    MutableLiveData<Resource> resource = new MutableLiveData<>();

    @Inject
    public NotesRepositoryImpl(NotesRepositoryRemoteImpl notesRepositoryRemote,
                               NetworkHelper networkHelper, NoteDao noteDao, RxSchedulers rxSchedulers,
                               ErrorMessages errorMessages) {
        this.notesRepositoryRemote = notesRepositoryRemote;
        this.networkHelper = networkHelper;
        this.noteDao = noteDao;
        this.rxSchedulers = rxSchedulers;
        this.errorMessages = errorMessages;
    }

    @SuppressLint("CheckResult")
    @Override
    public NotesResult getNotes() {
        LiveData<PagedList<NoteModel>> liveData =
                new LivePagedListBuilder(noteDao.getAllNoteEntities()
                        .map(input -> new NoteModel(input)),
                        PAGE_SIZE)
                        .build();

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

        return new NotesResult(liveData, resource);
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
        } else  {
            return Observable.error(new Throwable(errorMessages.getDeletingNoteNotImplementedOfflineMessage()));
        }
    }

    @Override
    public Observable<NoteModel> getNote(Long id) {
        return notesRepositoryRemote.getNote(id);
    }
}

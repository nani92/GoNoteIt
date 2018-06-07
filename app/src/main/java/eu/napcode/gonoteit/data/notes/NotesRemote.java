package eu.napcode.gonoteit.data.notes;

import android.annotation.SuppressLint;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.GetNoteByIdQuery;
import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.UpdateNoteMutation;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.rx.RxSchedulers;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class NotesRemote {

    private final RxSchedulers rxSchedulers;
    private StoreAuth storeAuth;
    private ApolloClient apolloClient;
    private ApolloRxHelper apolloRxHelper;

    @Inject
    public NotesRemote(ApolloClient apolloClient, StoreAuth storeAuth, ApolloRxHelper apolloRxHelper,
                       RxSchedulers rxSchedulers) {
        this.apolloClient = apolloClient;
        this.storeAuth = storeAuth;
        this.apolloRxHelper = apolloRxHelper;
        this.rxSchedulers = rxSchedulers;
    }

    @SuppressLint("CheckResult")
    public Observable<List<GetNotesQuery.AllEntity>> getNotes() {
        //TODO add token to query

        return apolloRxHelper
                .from(apolloClient.query(new GetNotesQuery()))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io())
                .flatMap(dataResponse -> Observable.fromArray(dataResponse.data().allEntities()));
    }

    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {
        Note note = new Note(noteModel);

        return apolloRxHelper
                .from(apolloClient.mutate(new CreateNoteMutation(note.getNoteDataString())))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread());
    }

    public Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id) {
        return apolloRxHelper
                .from(apolloClient.mutate(new DeleteNoteMutation(id)))
                .observeOn(rxSchedulers.io())
                .subscribeOn(rxSchedulers.androidMainThread());
    }

    public Observable<NoteModel> getNote(Long id) {
        return apolloRxHelper.from(apolloClient.query(new GetNoteByIdQuery(id)))
                .map(dataResponse -> dataResponse.data().entity())
                .map(entity -> ((Note) entity.data()).parseNote(entity));
    }

    public Observable<Response<UpdateNoteMutation.Data>> updateNote(NoteModel noteModel) {
        Note note = new Note(noteModel);

        return apolloRxHelper
                .from(apolloClient.mutate(new UpdateNoteMutation(noteModel.getId(), note.getNoteDataString())))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread());
    }
}

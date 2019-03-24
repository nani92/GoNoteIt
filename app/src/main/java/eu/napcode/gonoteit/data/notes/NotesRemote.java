package eu.napcode.gonoteit.data.notes;

import android.annotation.SuppressLint;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.GetChangelogMutation;
import eu.napcode.gonoteit.GetNoteByIdQuery;
import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.UpdateFavoritesMutation;
import eu.napcode.gonoteit.UpdateNoteMutation;
import eu.napcode.gonoteit.api.ApiEntity;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.rx.RxSchedulers;
import eu.napcode.gonoteit.utils.TimestampStore;
import io.reactivex.Observable;

import static eu.napcode.gonoteit.data.notes.DateInputKt.getDateInput;
import static eu.napcode.gonoteit.data.user.FavoritesMapperKt.favoritesMapToString;

public class NotesRemote {

    private final RxSchedulers rxSchedulers;
    private final TimestampStore timestampStore;
    private StoreAuth storeAuth;
    private ApolloClient apolloClient;
    private ApolloRxHelper apolloRxHelper;


    @Inject
    public NotesRemote(ApolloClient apolloClient, StoreAuth storeAuth, ApolloRxHelper apolloRxHelper,
                       RxSchedulers rxSchedulers, TimestampStore timestampStore) {
        this.apolloClient = apolloClient;
        this.storeAuth = storeAuth;
        this.apolloRxHelper = apolloRxHelper;
        this.rxSchedulers = rxSchedulers;
        this.timestampStore = timestampStore;
    }

    @SuppressLint("CheckResult")
    public Observable<List<GetNotesQuery.AllEntity>> getNotes() {
        //TODO add token to query

        return apolloRxHelper
                .from(apolloClient.query(new GetNotesQuery()))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io())
                .doOnNext(dataResponse ->
                        timestampStore.saveTimestamp(dataResponse.data().timestamp()))
                .flatMap(dataResponse -> Observable.fromArray(dataResponse.data().allEntities()));
    }

    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {
        Note note = new Note(noteModel);

        return apolloRxHelper
                .from(apolloClient.mutate(new CreateNoteMutation(
                        note.getNoteDataString(),
                        noteModel.getReadAccess(),
                        noteModel.getWriteAccess(),
                        getDateInput(noteModel.getDate()
                        ))))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io());
    }

    public Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id) {
        return apolloRxHelper
                .from(apolloClient.mutate(new DeleteNoteMutation(id)))
                .observeOn(rxSchedulers.io())
                .subscribeOn(rxSchedulers.androidMainThread());
    }

    public Observable<NoteModel> getNote(Long id) {
        return apolloRxHelper
                .from(apolloClient.query(new GetNoteByIdQuery(id)))
                .subscribeOn(rxSchedulers.io())
                .map(dataResponse -> dataResponse.data().entity())
                .map(entity -> ((Note) entity.data()).parseNote(new ApiEntity(entity)));
    }

    public Observable<Response<UpdateNoteMutation.Data>> updateNote(NoteModel noteModel) {
        Note note = new Note(noteModel);

        return apolloRxHelper
                .from(apolloClient.mutate(new UpdateNoteMutation(
                        noteModel.getId(),
                        note.getNoteDataString(),
                        noteModel.getReadAccess(),
                        noteModel.getWriteAccess(),
                        getDateInput(noteModel.getDate())
                )))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io());
    }

    public boolean shouldFetchChangelog() {
        return timestampStore.hasTimestamp();
    }

    public Observable<Response<GetChangelogMutation.Data>> getChangelog() {
        return apolloRxHelper
                .from(apolloClient.mutate(new GetChangelogMutation(timestampStore.getTimestamp())))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io());
    }

    public void saveTimestamp(Long timestamp) {
        timestampStore.saveTimestamp(timestamp);
    }

    public Observable<Response<UpdateFavoritesMutation.Data>> updateFavorites(List<Long> favorites) {
        return apolloRxHelper
                .from(apolloClient.mutate(new UpdateFavoritesMutation(favoritesMapToString(favorites))))
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io());
    }
}

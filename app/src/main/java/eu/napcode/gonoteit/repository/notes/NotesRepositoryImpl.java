package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.rx.RxSchedulers;
import eu.napcode.gonoteit.type.Type;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class NotesRepositoryImpl implements NotesRepository {

    private StoreAuth storeAuth;
    private ApolloClient apolloClient;
    private ApolloRxHelper apolloRxHelper;

    @Inject
    public NotesRepositoryImpl(ApolloClient apolloClient, StoreAuth storeAuth, ApolloRxHelper apolloRxHelper) {
        this.apolloClient = apolloClient;
        this.storeAuth = storeAuth;
        this.apolloRxHelper = apolloRxHelper;
    }

    @Override
    public Flowable<List<NoteModel>> getNotes() {
        //TODO add token to query

        return apolloRxHelper.from(apolloClient.query(new GetNotesQuery()))
                .flatMap(dataResponse -> Observable.fromArray(dataResponse.data().allEntities()))
                .flatMapIterable(listOfEntities -> listOfEntities)
                .filter(allEntity -> allEntity.type() != Type.NONE)
                .map(allEntity -> ((NoteModel) ((Note) allEntity.data()).parseNote(allEntity.type())))
                .toList()
                .toFlowable();
    }
}

package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.api.Note;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.dao.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class NotesRepositoryRemoteImpl implements NotesRepository {

    private StoreAuth storeAuth;
    private ApolloClient apolloClient;
    private ApolloRxHelper apolloRxHelper;
    private NoteDao noteDao;

    @Inject
    public NotesRepositoryRemoteImpl(ApolloClient apolloClient, StoreAuth storeAuth, ApolloRxHelper apolloRxHelper, NoteDao noteDao) {
        this.apolloClient = apolloClient;
        this.storeAuth = storeAuth;
        this.apolloRxHelper = apolloRxHelper;
        this.noteDao = noteDao;
    }

    @Override
    public Flowable<List<NoteModel>> getNotes() {
        //TODO add token to query

        return apolloRxHelper.from(apolloClient.query(new GetNotesQuery()))
                .flatMap(dataResponse -> Observable.fromArray(dataResponse.data().allEntities()))
                .flatMapIterable(listOfEntities -> listOfEntities)
                .filter(allEntity -> allEntity.type() != Type.NONE)
                .map(allEntity -> ((NoteModel) ((Note) allEntity.data()).parseNote(allEntity.type(), allEntity.uuid())))
                .doOnEach(noteModelNotification -> noteDao.insertNote(new NoteEntity(noteModelNotification.getValue())))
                .toList()
                .toFlowable();
    }

    @Override
    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {
        Input<String> title = Input.fromNullable(noteModel.getTitle());
        Input<String> content = Input.fromNullable(noteModel.getContent());

        return apolloRxHelper.from(apolloClient.mutate(new CreateNoteMutation(title, content)));
    }
}

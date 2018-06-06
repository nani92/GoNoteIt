package eu.napcode.gonoteit.repository.notes;

import android.annotation.SuppressLint;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.GetNoteByIdQuery;
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

public class NotesRepositoryRemoteImpl {

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

    @SuppressLint("CheckResult")
    public Flowable<List<NoteModel>> getNotes() {
        //TODO add token to query

        return apolloRxHelper.from(apolloClient.query(new GetNotesQuery()))
                .flatMap(dataResponse -> {
                    //noteDao.removeAll();

                    return Observable.fromArray(dataResponse.data().allEntities());
                })
                .flatMapIterable(listOfEntities -> listOfEntities)
                .filter(allEntity -> allEntity.type() != Type.NONE)
                .map(allEntity -> (NoteModel) ((Note) allEntity.data()).parseNote(allEntity.type(), allEntity.uuid(), allEntity.id()))
                .doOnEach(noteModelNotification -> {

                    if (noteModelNotification.getValue() != null) {
                        noteDao.insertNote(new NoteEntity(noteModelNotification.getValue()));
                    }
                })
                .toList()
                .toFlowable();
    }

    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {
        Note note = new Note(noteModel);

        return apolloRxHelper.from(apolloClient.mutate(new CreateNoteMutation(note.getNoteString())));
    }

    public Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id) {
        return apolloRxHelper.from(apolloClient.mutate(new DeleteNoteMutation(id)));
    }

    public Observable<NoteModel> getNote(Long id) {
        return apolloRxHelper.from(apolloClient.query(new GetNoteByIdQuery(id)))
                .map(dataResponse -> dataResponse.data().entity())
                .map(entity -> (NoteModel) ((Note)entity.data()).parseNote(entity.type(), entity.uuid(), entity.id()));
    }
}

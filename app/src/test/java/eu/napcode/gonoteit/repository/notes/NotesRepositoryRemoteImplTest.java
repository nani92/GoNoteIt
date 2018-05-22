package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.dao.NoteDao;
import eu.napcode.gonoteit.model.note.NoteModel;
import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class NotesRepositoryRemoteImplTest {


    @Mock
    ApolloClient apolloClient;

    @Mock
    StoreAuth storeAuth;

    @Mock
    ApolloRxHelper apolloRxHelper;

    @Mock
    ApolloQueryCall<GetNotesQuery.Data> apolloGetNotesCall;

    @Mock
    Response<GetNotesQuery.Data> getNotesResponse;

    @Mock
    GetNotesQuery.Data data;

    @Mock
    GetNotesQuery.AllEntity allEntity;

    @Mock
    NoteDao noteDao;

    private NotesRepositoryRemoteImpl notesRepositoryRemote;

    @Before
    public void init() {
        this.notesRepositoryRemote = new NotesRepositoryRemoteImpl(apolloClient, storeAuth, apolloRxHelper, noteDao);

        Mockito.when(apolloClient.query(Mockito.any(GetNotesQuery.class)))
                .thenReturn(apolloGetNotesCall);

        Mockito.when(apolloRxHelper.from(apolloGetNotesCall))
                .thenReturn(Observable.just(getNotesResponse));
    }


    @Test
    public void testCallGetNotesQuery() {
        notesRepositoryRemote.getNotes();

        Mockito.verify(apolloClient).query(Mockito.any(GetNotesQuery.class));
    }

    @Test
    public void testCallIntoRx() {
        notesRepositoryRemote.getNotes();

        Mockito.verify(apolloRxHelper).from(apolloGetNotesCall);
    }

    @Test
    public void testReturnNotes() {
        TestSubscriber<List<NoteModel>> notesSubscriber = new TestSubscriber<>();
        notesRepositoryRemote.getNotes().subscribe(notesSubscriber);

        notesSubscriber.assertSubscribed();
    }
}
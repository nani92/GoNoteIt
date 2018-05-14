package eu.napcode.gonoteit.repository.notes;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import eu.napcode.gonoteit.GetNotesQuery;
import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.api.ApolloRxHelper;
import eu.napcode.gonoteit.auth.StoreAuth;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.type.Type;
import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;


@RunWith(MockitoJUnitRunner.class)
public class NotesRepositoryTest {

    private NotesRepository notesRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

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

    @Before
    public void init() {
        this.notesRepository = new NotesRepositoryImpl(apolloClient, storeAuth, apolloRxHelper);

        Mockito.when(apolloClient.query(Mockito.any(GetNotesQuery.class)))
                .thenReturn(apolloGetNotesCall);

        Mockito.when(apolloRxHelper.from(apolloGetNotesCall))
                .thenReturn(Observable.just(getNotesResponse));
    }

    @Test
    public void testCallGetNotesQuery() {
        notesRepository.getNotes();

        Mockito.verify(apolloClient).query(Mockito.any(GetNotesQuery.class));
    }

    @Test
    public void testCallIntoRx() {
        notesRepository.getNotes();

        Mockito.verify(apolloRxHelper).from(apolloGetNotesCall);
    }

    @Test
    public void testReturnNotes() {
        TestSubscriber<List<NoteModel>> notesSubscriber = new TestSubscriber<>();
        notesRepository.getNotes().subscribe(notesSubscriber);

        notesSubscriber.assertSubscribed();
    }
}
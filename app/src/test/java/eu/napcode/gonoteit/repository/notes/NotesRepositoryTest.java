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
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;


@RunWith(MockitoJUnitRunner.class)
public class NotesRepositoryTest {

    private NotesRepository notesRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    NotesRepositoryLocalImpl notesRepositoryLocal;

    @Mock
    NotesRepositoryRemoteImpl notesRepositoryRemote;

    @Mock
    NetworkHelper networkHelper;

    @Before
    public void init() {
        this.notesRepository = new NotesRepositoryImpl(notesRepositoryRemote, notesRepositoryLocal, networkHelper);
    }

    @Test
    public void testCallRemoteGetNotesQuery() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(true);

        this.notesRepository.getNotes();

        Mockito.verify(notesRepositoryRemote).getNotes();
    }

    @Test
    public void testCallLocalGetNotesQuery() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(false);

        this.notesRepository.getNotes();

        Mockito.verify(notesRepositoryLocal).getNotes();
    }
}
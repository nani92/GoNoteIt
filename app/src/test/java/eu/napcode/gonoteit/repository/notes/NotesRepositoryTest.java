package eu.napcode.gonoteit.repository.notes;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.apollographql.apollo.api.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import eu.napcode.gonoteit.GetChangelogMutation;
import eu.napcode.gonoteit.data.notes.NotesLocal;
import eu.napcode.gonoteit.data.notes.NotesRemote;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class NotesRepositoryTest {

    private NotesRepository notesRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    NotesLocal notesLocal;

    @Mock
    NotesRemote notesRemote;

    @Mock
    NetworkHelper networkHelper;

    @Mock
    ErrorMessages errorMessages;

    @Mock
    Response<GetChangelogMutation.Data> changelogResponse;

    @Before
    public void init() {
        this.notesRepository = new NotesRepositoryImpl(notesRemote, notesLocal, networkHelper, errorMessages);
    }

    @Test
    public void testGetNotesForNetworkAvailable() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(true);
        Mockito.when(notesRemote.getNotes())
                .thenReturn(Observable.just(new ArrayList<>()));

        this.notesRepository.getNotes();

        Mockito.verify(notesLocal).getNotes();
        Mockito.verify(notesRemote).getNotes();
    }

    @Test
    public void testGetNotesForNetworkAvailableAndTimestampStored() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(true);
        Mockito.when(notesRemote.getChangelog())
                .thenReturn(Observable.just(changelogResponse));
        Mockito.when(notesRemote.shouldFetchChangelog()).thenReturn(true);

        this.notesRepository.getNotes();

        Mockito.verify(notesLocal).getNotes();
        Mockito.verify(notesRemote, times(0)).getNotes();
        Mockito.verify(notesRemote).getChangelog();
    }

    @Test
    public void testGetNotesForNetworkNotAvailable() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(false);

        this.notesRepository.getNotes();

        Mockito.verify(notesLocal).getNotes();
        Mockito.verify(notesRemote, times(0)).getChangelog();
        Mockito.verify(notesRemote, times(0)).getNotes();
    }
}
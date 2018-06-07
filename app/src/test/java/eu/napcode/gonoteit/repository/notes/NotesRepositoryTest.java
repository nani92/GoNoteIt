package eu.napcode.gonoteit.repository.notes;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import eu.napcode.gonoteit.data.notes.NotesLocal;
import eu.napcode.gonoteit.data.notes.NotesRemote;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;

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

    @Before
    public void init() {
        this.notesRepository = new NotesRepositoryImpl(notesRemote, notesLocal, networkHelper, errorMessages);
    }

    @Test
    public void testCallRemoteGetNotesQuery() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(true);
        Mockito.when(notesRemote.getNotes())
                .thenReturn(Observable.just(new ArrayList<>()));

        this.notesRepository.getNotes();

        Mockito.verify(notesRemote).getNotes();
    }

    @Test
    public void testCallLocalGetNotesQuery() {
        Mockito.when(networkHelper.isNetworkAvailable())
                .thenReturn(false);

        this.notesRepository.getNotes();

        Mockito.verify(notesLocal).getNotes();
    }
}
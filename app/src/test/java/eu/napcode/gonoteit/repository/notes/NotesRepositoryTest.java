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

import eu.napcode.gonoteit.utils.NetworkHelper;

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
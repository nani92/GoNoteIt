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

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.GetChangelogMutation;
import eu.napcode.gonoteit.data.notes.NotesLocal;
import eu.napcode.gonoteit.data.notes.NotesRemote;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.utils.ErrorMessages;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyLong;
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
        this.notesRepository.getNotes();

        Mockito.verify(notesLocal).getNotes();
        Mockito.verify(notesRemote, times(0)).getChangelog();
        Mockito.verify(notesRemote, times(0)).getNotes();
    }

    @Mock
    NoteModel noteModel;

    @Mock
    Response<CreateNoteMutation.Data> createNoteResponse;

    @Test
    public void testCreateNoteForNetworkAvailable() {
        Mockito.when(networkHelper.isNetworkAvailable()).thenReturn(true);
        Mockito.when(notesRemote.createNote(noteModel)).thenReturn(Observable.just(createNoteResponse));

        this.notesRepository.createNote(noteModel);

        Mockito.verify(notesRemote).createNote(noteModel);
    }

    @Test
    public void testCreateNoteForNetworkNotAvailable() {
        this.notesRepository.createNote(noteModel);

        Mockito.verify(notesRemote, times(0)).createNote(noteModel);
    }

    @Mock
    Response<DeleteNoteMutation.Data> deleteNoteResponse;

    @Test
    public void testDeleteNoteForNetworkAvailable() {
        Mockito.when(networkHelper.isNetworkAvailable()).thenReturn(true);
        Mockito.when(notesRemote.deleteNote(anyLong())).thenReturn(Observable.just(deleteNoteResponse));
        Long noteId = 2l;

        notesRepository.deleteNote(noteId);

        Mockito.verify(notesRemote).deleteNote(noteId);
    }

    @Test
    public void testDeleteNoteForNetworkNotAvailable() {
        Long noteId = 2l;

        notesRepository.deleteNote(noteId);

        Mockito.verify(notesRemote, times(0)).deleteNote(noteId);
    }
}
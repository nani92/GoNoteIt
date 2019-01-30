package eu.napcode.gonoteit.ui.notes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import io.reactivex.Flowable;

@RunWith(MockitoJUnitRunner.class)
public class NotesViewModelTest {

    private NotesViewModel notesViewModel;

    @Mock
    NotesRepository notesRepository;

    @Mock
    NotesResult notesResult;

    @Before
    public void init() {
        this.notesViewModel = new NotesViewModel(notesRepository);

        Mockito.when(notesRepository.getNotes()).thenReturn(notesResult);
    }

    @Test
    public void testCallingGetNotes() {
        this.notesViewModel.getNotes();

        Mockito.verify(notesRepository).getNotes();
    }
}
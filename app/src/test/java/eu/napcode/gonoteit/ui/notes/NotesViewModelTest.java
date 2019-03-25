package eu.napcode.gonoteit.ui.notes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import eu.napcode.gonoteit.data.notes.results.NotesResult;
import eu.napcode.gonoteit.repository.notes.NotesRepository;

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
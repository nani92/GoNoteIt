package eu.napcode.gonoteit.ui.notes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import eu.napcode.gonoteit.MockRxSchedulers;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import io.reactivex.Flowable;

@RunWith(MockitoJUnitRunner.class)
public class NotesViewModelTest {

    private NotesViewModel notesViewModel;

    @Mock
    NotesRepository notesRepository;

    @Before
    public void init() {
        this.notesViewModel = new NotesViewModel(notesRepository, new MockRxSchedulers());
    }

    @Test
    public void testCallingGetNotes() {
        Mockito.when(notesRepository.getNotes())
                .thenReturn(Flowable.just(Arrays.asList(new NoteModel())));

        this.notesViewModel.getNotes();

        Mockito.verify(notesRepository).getNotes();
    }
}
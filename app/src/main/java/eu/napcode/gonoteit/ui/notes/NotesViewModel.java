package eu.napcode.gonoteit.ui.notes;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.data.results.DeletedResult;
import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.notes.NotesRepository;

public class NotesViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public NotesViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public NotesResult getNotes() {
        return notesRepository.getNotes();
    }


    public DeletedResult deleteNote(Long id) {
        return notesRepository.deleteNote(id);
    }
}

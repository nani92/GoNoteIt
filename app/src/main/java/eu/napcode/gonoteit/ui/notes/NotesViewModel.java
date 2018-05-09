package eu.napcode.gonoteit.ui.notes;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.notes.NotesRepository;

public class NotesViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public NotesViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }
}

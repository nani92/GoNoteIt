package eu.napcode.gonoteit.ui.create;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.notes.NotesRepository;

public class CreateViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public CreateViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }
}

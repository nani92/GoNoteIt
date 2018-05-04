package eu.napcode.gonoteit.ui.main;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.NotesRepository;

public class MainViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public MainViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }


}

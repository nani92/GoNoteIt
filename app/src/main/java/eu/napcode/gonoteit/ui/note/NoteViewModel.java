package eu.napcode.gonoteit.ui.note;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.repository.notes.NotesRepository;

public class NoteViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public NoteViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }
}

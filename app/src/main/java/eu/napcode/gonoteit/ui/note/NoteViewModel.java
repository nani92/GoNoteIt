package eu.napcode.gonoteit.ui.note;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.data.results.NoteResult;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.notes.NotesRepository;

public class NoteViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public NoteViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @SuppressLint("CheckResult")
    public NoteResult getNote(Long id) {
        return this.notesRepository.getNote(id);
    }

    @SuppressLint("CheckResult")
    public LiveData<Resource> createNote(NoteModel noteModel) {
        return notesRepository.createNote(noteModel);
    }

    public LiveData<Resource> updateFavorites(Long id) {
        return notesRepository.updateFavorites(id);
    }

    public LiveData<Boolean> isNoteFavorite(Long id) {
        return notesRepository.isNoteFavorite(id);
    }
}

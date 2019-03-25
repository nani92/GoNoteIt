package eu.napcode.gonoteit.ui.create;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.data.notes.results.NoteResult;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.notes.NotesRepository;

public class CreateViewModel extends ViewModel {

    private NotesRepository notesRepository;

    @Inject
    public CreateViewModel(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @SuppressLint("CheckResult")
    public LiveData<Resource<?>> createNote(NoteModel noteModel) {

        if (noteModel.getId() != null) {
            return notesRepository.updateNote(noteModel);
        }

        return notesRepository.createNote(noteModel);
    }

    public NoteResult getNote(Long id) {
        return notesRepository.getNote(id);
    }
}

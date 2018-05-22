package eu.napcode.gonoteit.repository.notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.dao.NoteEntity;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Flowable;
import timber.log.Timber;

public class NotesRepositoryImpl implements NotesRepository {

    private NotesRepositoryLocalImpl notesRepositoryLocal;
    private NotesRepositoryRemoteImpl notesRepositoryRemote;
    private NetworkHelper networkHelper;

    @Inject
    public NotesRepositoryImpl(NotesRepositoryRemoteImpl notesRepositoryRemote,
                               NotesRepositoryLocalImpl notesRepositoryLocal,
                               NetworkHelper networkHelper) {
        this.notesRepositoryLocal = notesRepositoryLocal;
        this.notesRepositoryRemote = notesRepositoryRemote;
        this.networkHelper = networkHelper;
    }

    @Override
    public Flowable<List<NoteModel>> getNotes() {

        if (networkHelper.isNetworkAvailable()) {
            return this.notesRepositoryRemote.getNotes();
        } else {
             return this.notesRepositoryLocal.getNotes();
        }
    }
}

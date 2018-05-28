package eu.napcode.gonoteit.repository.notes;

import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.DeleteNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.utils.NetworkHelper;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class  NotesRepositoryImpl implements NotesRepository {

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

    @Override
    public Observable<Response<CreateNoteMutation.Data>> createNote(NoteModel noteModel) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRepositoryRemote.createNote(noteModel);
        } else {
            return notesRepositoryLocal.createNote(noteModel);
        }
    }

    @Override
    public Observable<Response<DeleteNoteMutation.Data>> deleteNote(Long id) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRepositoryRemote.deleteNote(id);
        } else {
            return notesRepositoryLocal.deleteNote(id);
        }
    }

    @Override
    public Observable<NoteModel> getNote(Long id) {

        if (networkHelper.isNetworkAvailable()) {
            return notesRepositoryRemote.getNote(id);
        } else {
            return notesRepositoryLocal.getNote(id);
        }
    }
}

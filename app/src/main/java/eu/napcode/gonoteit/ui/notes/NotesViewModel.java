package eu.napcode.gonoteit.ui.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import eu.napcode.gonoteit.rx.RxSchedulers;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<Resource<List<NoteModel>>> notesLiveData = new MutableLiveData<>();
    private NotesRepository notesRepository;
    private RxSchedulers rxSchedulers;

    @Inject
    public NotesViewModel(NotesRepository notesRepository, RxSchedulers rxSchedulers) {
        this.notesRepository = notesRepository;
        this.rxSchedulers =rxSchedulers;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<Resource<List<NoteModel>>> getNotes() {
        notesRepository.getNotes()
                .observeOn(rxSchedulers.io())
                .subscribeOn(rxSchedulers.androidMainThread())
                .doOnSubscribe(it -> notesLiveData.postValue(Resource.loading(null)))
                .subscribe(notes -> notesLiveData.postValue(Resource.success(notes)),
                        throwable -> notesLiveData.postValue(Resource.error(throwable)));

        return notesLiveData;
    }
}

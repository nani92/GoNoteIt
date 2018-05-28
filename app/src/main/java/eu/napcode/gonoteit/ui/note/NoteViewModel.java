package eu.napcode.gonoteit.ui.note;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.R;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import eu.napcode.gonoteit.rx.RxSchedulers;

public class NoteViewModel extends ViewModel {

    private final RxSchedulers rxSchedulers;
    private NotesRepository notesRepository;
    private MutableLiveData<Resource<NoteModel>> noteLiveData = new MutableLiveData<>();

    @Inject
    public NoteViewModel(NotesRepository notesRepository, RxSchedulers rxSchedulers) {
        this.notesRepository = notesRepository;
        this.rxSchedulers = rxSchedulers;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<Resource<NoteModel>> getNote(Long id) {
        this.notesRepository.getNote(id)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .doOnSubscribe(it -> noteLiveData.postValue(Resource.loading(null)))
                .subscribe(noteModel -> noteLiveData.postValue(Resource.success(noteModel)),
                        error -> noteLiveData.postValue(Resource.error(error)));

        return noteLiveData;
    }
}

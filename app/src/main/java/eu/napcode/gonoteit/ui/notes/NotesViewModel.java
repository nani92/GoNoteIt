package eu.napcode.gonoteit.ui.notes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.napcode.gonoteit.data.results.NotesResult;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import eu.napcode.gonoteit.rx.RxSchedulers;

public class NotesViewModel extends ViewModel {

    private NotesResult notesResult;
    private NotesRepository notesRepository;
    private RxSchedulers rxSchedulers;

    @Inject
    public NotesViewModel(NotesRepository notesRepository, RxSchedulers rxSchedulers) {
        this.notesRepository = notesRepository;
        this.rxSchedulers = rxSchedulers;
    }

    public NotesResult getNotes() {
        return notesRepository.getNotes();
    }

    @SuppressLint("CheckResult")
    public LiveData<Resource<Boolean>> deleteNote(Long id) {
        MutableLiveData<Resource<Boolean>> deleted = new MutableLiveData<>();

        notesRepository.deleteNote(id)
                .observeOn(rxSchedulers.io())
                .subscribeOn(rxSchedulers.androidMainThread())
                .doOnSubscribe(it -> deleted.postValue(Resource.loading(null)))
                .filter(response -> response.data() != null && response.data().deleteEntity() != null)
                .singleOrError()
                .subscribe(dataResponse -> {

                            if (dataResponse.data().deleteEntity().deleted()) {
                                deleted.postValue(Resource.success(true));
                                getNotes();
                            } else {
                                deleted.postValue(Resource.error(new Throwable()));
                            }
                        },
                        error -> deleted.postValue(Resource.error(error)));

        return deleted;
    }
}

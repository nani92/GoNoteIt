package eu.napcode.gonoteit.ui.create;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.apollographql.apollo.api.Response;

import javax.inject.Inject;

import eu.napcode.gonoteit.CreateNoteMutation;
import eu.napcode.gonoteit.model.note.NoteModel;
import eu.napcode.gonoteit.repository.Resource;
import eu.napcode.gonoteit.repository.notes.NotesRepository;
import eu.napcode.gonoteit.rx.RxSchedulers;
import io.reactivex.Observable;

public class CreateViewModel extends ViewModel {

    private NotesRepository notesRepository;
    private RxSchedulers rxSchedulers;
    private MutableLiveData<Resource> createNoteLiveData = new MutableLiveData<>();

    @Inject
    public CreateViewModel(NotesRepository notesRepository, RxSchedulers rxSchedulers) {
        this.notesRepository = notesRepository;
        this.rxSchedulers = rxSchedulers;
    }

    @SuppressLint("CheckResult")
    public LiveData<Resource> createNote(NoteModel noteModel) {
        notesRepository.createNote(noteModel)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .doOnSubscribe(it -> createNoteLiveData.postValue(Resource.loading(null)))
                .subscribe(response -> {
                            if (response.data() == null ||
                                    response.data().createEntity() == null ||
                                    response.data().createEntity().ok() == false) {
                                createNoteLiveData.postValue(Resource.error(new Throwable()));
                            } else {
                                createNoteLiveData.postValue(Resource.success(null));

                            }
                        },
                        error -> createNoteLiveData.postValue(Resource.error(error)));

        return createNoteLiveData;
    }
}

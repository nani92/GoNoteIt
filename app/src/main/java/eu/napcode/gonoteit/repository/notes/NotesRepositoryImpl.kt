package eu.napcode.gonoteit.repository.notes

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import eu.napcode.gonoteit.CreateNoteMutation
import eu.napcode.gonoteit.UpdateNoteMutation

import javax.inject.Inject

import eu.napcode.gonoteit.api.ApiEntity
import eu.napcode.gonoteit.api.Note
import eu.napcode.gonoteit.data.notes.NotesLocal
import eu.napcode.gonoteit.data.notes.NotesRemote
import eu.napcode.gonoteit.data.results.DeletedResult
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.data.results.NoteResult
import eu.napcode.gonoteit.data.results.NotesResult
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.repository.user.UserRepository
import eu.napcode.gonoteit.utils.ErrorMessages
import eu.napcode.gonoteit.utils.NetworkHelper

class NotesRepositoryImpl @Inject
constructor(private val notesRemote: NotesRemote, private val notesLocal: NotesLocal,
            private val networkHelper: NetworkHelper, private val errorMessages: ErrorMessages,
            private val userRepository: UserRepository) : NotesRepository {
    internal var resource = MutableLiveData<Resource<*>>()

    override fun getNotes(): NotesResult {

        if (networkHelper.isNetworkAvailable) {
            updateNotesFromRemote()
        } else {
            resource.postValue(Resource.error<Any>(Throwable(errorMessages.offlineMessage)))
        }

        return NotesResult(notesLocal.notes, resource)
    }

    private fun updateNotesFromRemote() {

        if (notesRemote.shouldFetchChangelog()) {
            getNotesChangelog()
        } else {
            getNotesFromRemote()
        }
    }

    @SuppressLint("CheckResult")
    private fun getNotesChangelog() {
        //TODO Clean it up
        notesRemote.changelog
                .doOnSubscribe { it -> resource.postValue(Resource.loading<Any>(null)) }
                //                .filter(dataResponse -> dataResponse.hasErrors())
                //                .filter(dataResponse -> dataResponse.data().changelog() != null)
                //                .singleOrError()
                //                .map(dataResponse -> dataResponse.data().changelog())
                //                .doOnSuccess(it -> notesLocal.saveChangelog(it))
                //                .doOnSuccess(it -> notesRemote.saveTimestamp(it.timestamp()))
                .subscribe(
                        { dataResponse ->

                            if (dataResponse.hasErrors()) {
                                resource.postValue(Resource.success<Any>(null))
                                //TODO auth error already displayed
                                //    resource.postValue(Resource.error(dataResponse.errors().get(0)));
                            } else {
                                resource.postValue(Resource.success<Any>(null))
                            }

                            if (dataResponse.data()!!.changelog() != null) {
                                notesLocal.saveChangelog(dataResponse.data()!!.changelog()!!)
                                notesRemote.saveTimestamp(dataResponse.data()!!.changelog()!!.timestamp())
                            }
                        },
                        { error -> resource.postValue(Resource.error<Any>(error)) }
                )
    }

    @SuppressLint("CheckResult")
    private fun getNotesFromRemote() {
        notesRemote.notes
                .doOnSubscribe { it -> resource.postValue(Resource.loading<Any>(null)) }
                .subscribe(
                        { data ->
                            resource.postValue(Resource.success<Any>(null))
                            notesLocal.saveEntities(data)
                        },
                        { error -> resource.postValue(Resource.error<Any>(error)) }
                )
    }

    override fun createNote(noteModel: NoteModel): LiveData<Resource<*>> {

        if (networkHelper.isNetworkAvailable) {
            createNoteOnRemote(noteModel)
        } else {
            resource.postValue(Resource.error<Any>(Throwable(errorMessages.creatingNoteNotImplementedOfflineMessage)))
        }

        return resource
    }

    @SuppressLint("CheckResult")
    private fun createNoteOnRemote(noteModel: NoteModel) {
        notesRemote.createNote(noteModel)
                .doOnSubscribe { it -> resource.postValue(Resource.loading<Any>(null)) }
                .filter { response -> response.data() != null }
                .filter { response -> response.data()!!.createEntity() != null }
                .filter { response -> response.data()!!.createEntity()!!.ok()!! }
                .singleOrError()
                .doOnSuccess { it -> getNotesChangelog() }
                .map<CreateNoteMutation.Entity> { dataResponse -> dataResponse.data()!!.createEntity()!!.entity() }
                .map { entity -> (entity.data() as Note).parseNote(ApiEntity(entity)) as NoteModel }
                .doOnSuccess { notesLocal.saveEntity(it) }
                .subscribe(
                        { response -> resource.postValue(Resource.success<Any>(null)) },
                        { error -> resource.postValue(Resource.error<Any>(error)) }
                )
    }

    override fun deleteNote(id: Long?): DeletedResult {
        val resource = MutableLiveData<Resource<*>>()

        if (networkHelper.isNetworkAvailable) {
            deleteNoteOnRemote(id, resource)
        } else {
            resource.postValue(Resource.error<Any>(Throwable(errorMessages.deletingNoteNotImplementedOfflineMessage)))
        }

        return DeletedResult(id, resource)
    }

    @SuppressLint("CheckResult")
    private fun deleteNoteOnRemote(id: Long?, resource: MutableLiveData<Resource<*>>) {
        notesRemote.deleteNote(id)
                .doOnSubscribe { it -> resource.postValue(Resource.loading<Any>(null)) }
                .filter { response -> response.data() != null }
                .filter { response -> response.data()!!.deleteEntity() != null }
                .filter { response -> response.data()!!.deleteEntity()!!.deleted()!! }
                .singleOrError()
                .doOnSuccess { it -> getNotesChangelog() }
                .subscribe(
                        { response -> resource.postValue(Resource.success<Any>(null)) },
                        { error -> resource.postValue(Resource.error<Any>(error)) }
                )
    }

    override fun getNote(id: Long?): NoteResult {

        if (networkHelper.isNetworkAvailable) {
            getNoteFromRemote(id)
        } else {
            resource.postValue(Resource.error<Any>(Throwable(errorMessages.offlineMessage)))
        }

        return NoteResult(notesLocal.getNote(id), resource)
    }

    @SuppressLint("CheckResult")
    private fun getNoteFromRemote(id: Long?) {
        notesRemote.getNote(id)
                .doOnSubscribe { it -> resource.postValue(Resource.loading<Any>(null)) }
                .subscribe(
                        { noteModel ->
                            notesLocal.saveEntity(noteModel)
                            resource.postValue(Resource.success<Any>(null))
                        },
                        { error -> resource.postValue(Resource.error<Any>(error)) }
                )
    }

    override fun updateNote(noteModel: NoteModel): LiveData<Resource<*>> {

        if (networkHelper.isNetworkAvailable) {
            updateNoteOnRemote(noteModel)
        } else {
            resource.postValue(Resource.error<Any>(errorMessages.updatingNoteNotImplementedOfflineMessage))
        }

        return resource
    }


    @SuppressLint("CheckResult")
    private fun updateNoteOnRemote(noteModel: NoteModel) {
        notesRemote.updateNote(noteModel)
                .doOnSubscribe { it -> resource.postValue(Resource.loading<Any>(null)) }
                .filter { response -> response.data() != null }
                .filter { response -> response.data()!!.updateEntity() != null }
                .filter { response -> response.data()!!.updateEntity()!!.ok()!! }
                .singleOrError()
                .map<UpdateNoteMutation.Entity> { dataResponse -> dataResponse.data()!!.updateEntity()!!.entity() }
                .map { entity -> (entity.data() as Note).parseNote(ApiEntity(entity)) as NoteModel }
                .doOnSuccess { notesLocal.saveEntity(it) }
                .doOnSuccess { it -> getNotesChangelog() }
                .subscribe(
                        { response -> resource.postValue(Resource.success<Any>(null)) },
                        { error -> resource.postValue(Resource.error<Any>(error)) }
                )
    }

    override fun updateFavorites(id: Long): LiveData<Resource<*>> {
        val list = mutableListOf<Long>()
        list.addAll(notesLocal.getCurrentFavoriteIdsList())
        list.add(id)

        if (networkHelper.isNetworkAvailable) {
            notesRemote
                    .updateFavorites(list)
                    .doOnComplete { userRepository.updateUserFromRemote(null) }
                    .subscribe()
        } else {
            resource.postValue(Resource.error<Any>(errorMessages.updatingNoteNotImplementedOfflineMessage))
        }

        return resource
    }

    override fun getFavoriteNotes(): NotesResult {
        return NotesResult(notesLocal.getFavoriteNotes(), resource)
    }

    override fun isNoteFavorite(id: Long?): LiveData<Boolean> {
        return Transformations.map(notesLocal.getFavoriteIdsList()) {
            it.contains(id)
        }
    }
}

package eu.napcode.gonoteit.data.notes

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import eu.napcode.gonoteit.GetChangelogMutation

import javax.inject.Inject

import eu.napcode.gonoteit.GetChangelogMutation.Changelog
import eu.napcode.gonoteit.GetNotesQuery.AllEntity
import eu.napcode.gonoteit.api.ApiEntity
import eu.napcode.gonoteit.api.Note
import eu.napcode.gonoteit.dao.note.NoteDaoManipulator
import eu.napcode.gonoteit.dao.note.NoteEntity
import eu.napcode.gonoteit.dao.user.UserDao
import eu.napcode.gonoteit.model.UserModel
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.type.Type
import io.reactivex.Observable

class NotesLocal @Inject
constructor(private val noteDao: NoteDaoManipulator, private val userDao: UserDao) {

    val notes: LiveData<PagedList<NoteModel>>
        get() = LivePagedListBuilder(noteDao.allNoteEntities
                .map { input -> NoteModel(input) }, PAGE_SIZE)
                .build()

    fun getFavoriteNotes(): LiveData<PagedList<NoteModel>> {
        return Transformations.switchMap(userDao.userEntityLiveData) {
            var userModel = UserModel(it)

            LivePagedListBuilder(noteDao.getFavoriteNoteEntities(userModel.favorites)
                    .map { noteEntity -> NoteModel(noteEntity) }, PAGE_SIZE)
                    .build()
        }
    }

    fun getNote(id: Long?): LiveData<NoteModel> {
        val noteEntityLiveData = noteDao.getNoteById(id)

        return Transformations.map(noteEntityLiveData) {
            NoteModel(it)
        }
    }

    @SuppressLint("CheckResult")
    fun saveEntities(entities: List<AllEntity>) {
        Observable.just(entities)
                .flatMapIterable { allEntities -> allEntities }
                .filter { allEntity -> allEntity.type() != Type.NONE }
                .map { allEntity -> (allEntity.data() as Note).parseNote(ApiEntity(allEntity)) as NoteModel }
                .map { NoteEntity(it) }
                .filter { noteEntity -> noteEntity != null }
                .doOnEach { it -> noteDao.insertNote(it.value) }
                .subscribe()
    }

    fun saveEntity(noteModel: NoteModel) {
        noteDao.insertNote(NoteEntity(noteModel))
    }

    fun deleteNote(id: Long?) {
        noteDao.deleteNote(id)
    }

    fun deleteAllNotes() {
        noteDao.deleteAll()
    }

    @SuppressLint("CheckResult")
    fun saveChangelog(changelog: Changelog) {
        getNoteModelsToSaveObservable(changelog)
                .subscribe { noteModel -> saveEntity(noteModel) }

        Observable.just(changelog.deleted()!!)
                .flatMapIterable { deleteds -> deleteds }
                .subscribe { deleted -> noteDao.deleteNote(deleted) }
    }

    private fun getNoteModelsToSaveObservable(changelog: Changelog): Observable<NoteModel> {
        return getCreatedsObservable(changelog)
                .mergeWith(getUpdatedsObservable(changelog))
    }

    private fun getUpdatedsObservable(changelog: Changelog): Observable<NoteModel> {
        return Observable.just<List<GetChangelogMutation.Updated>>(changelog.updated()!!)
                .flatMapIterable<GetChangelogMutation.Updated> { updateds -> updateds }
                .map { updated -> (updated.data() as Note).parseNote(ApiEntity(updated)) as NoteModel }
    }

    private fun getCreatedsObservable(changelog: Changelog): Observable<NoteModel> {
        return Observable.just<List<GetChangelogMutation.Created>>(changelog.created()!!)
                .flatMapIterable<GetChangelogMutation.Created> { createds -> createds }
                .map { created -> (created.data() as Note).parseNote(ApiEntity(created)) as NoteModel }
    }

    fun getCurrentFavoriteIdsList() : List<Long> {
        return UserModel(userDao.userEntity).favorites
    }

    fun getFavoriteIdsList() : LiveData<List<Long>> {
        return Transformations.map(userDao.userEntityLiveData) {
            UserModel(userDao.userEntity).favorites
        }
    }

    companion object {
        private val PAGE_SIZE = 20
    }
}

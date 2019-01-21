package eu.napcode.gonoteit.repository.user

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response

import javax.inject.Inject

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.GetUserQuery
import eu.napcode.gonoteit.api.ApolloRxHelper
import eu.napcode.gonoteit.api.Note
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.dao.user.UserDao
import eu.napcode.gonoteit.dao.user.UserEntity
import eu.napcode.gonoteit.data.notes.NotesLocal
import eu.napcode.gonoteit.data.user.UserRemote
import eu.napcode.gonoteit.data.user.mapUserDataStringToUserData
import eu.napcode.gonoteit.model.UserModel
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.rx.RxSchedulers
import eu.napcode.gonoteit.ui.login.UserValidator
import eu.napcode.gonoteit.utils.TimestampStore
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber

class UserRepositoryImpl @Inject
constructor(
        private val apolloClient: ApolloClient,
        private val storeAuth: StoreAuth,
        private val apolloRxHelper: ApolloRxHelper,
        private val timestampStore: TimestampStore,
        private val notesLocal: NotesLocal,
        private val userRemote: UserRemote,
        private val userDao: UserDao,
        private val rxSchedulers: RxSchedulers,
        private val userValidator: UserValidator) : UserRepository {

    override fun isUserLoggedIn(): Boolean {
        val token = storeAuth.token

        return token != null && token.length > 0
    }

    @SuppressLint("CheckResult")
    override fun authenticateUser(login: String, password: String, authResource: MutableLiveData<Resource<AuthenticateMutation.Data>>): Observable<Response<AuthenticateMutation.Data>> {
        val authMutation = apolloClient
                .mutate(AuthenticateMutation(login, password))

        return apolloRxHelper
                .from(authMutation)
                .doOnSubscribe { timestampStore.removeTimestamp() }
                .doOnNext { processAuthResponse(it, authResource) }
                .doOnComplete { retrieveUserData(authResource) }
    }

    private fun processAuthResponse(authMutation: Response<AuthenticateMutation.Data>, authResource: MutableLiveData<Resource<AuthenticateMutation.Data>>) {
        val tokenAuth = authMutation.data()!!.tokenAuth()

        if (tokenAuth != null && userValidator.isTokenValid(tokenAuth.token())) {
            storeAuth.saveToken(tokenAuth.token())
        } else {
            authResource.postValue(Resource.error<AuthenticateMutation.Data>(authMutation.errors()[0]))
        }
    }

    @SuppressLint("CheckResult")
    //TODO handle error
    private fun retrieveUserData(authResource: MutableLiveData<Resource<AuthenticateMutation.Data>>) {
        userRemote.getUser()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.io())
                .subscribe(
                        {
                            saveUserData(it)
                            finishAuthProcess(authResource)
                        },
                        { Timber.d("get user error") })
    }

    private fun saveUserData(userData: GetUserQuery.User?) {
        var userModel = UserModel(userData!!.username()!!)
        mapUserDataStringToUserData(userModel, (userData.data() as Note).noteDataString)
        userDao.insertUser(UserEntity(userModel))

    }

    private fun finishAuthProcess(authResource: MutableLiveData<Resource<AuthenticateMutation.Data>>) {
        authResource.postValue(Resource.success<AuthenticateMutation.Data>(null))
    }

    override fun getLoggedInUser(): LiveData<UserModel?> {

        return Transformations.map(userDao.userEntity) {
            if (it == null) {
                return@map null
            } else {
                return@map UserModel(it)
            }
        }
    }

    override fun logoutUser(): Completable {
        return Completable
                .fromAction { notesLocal.deleteAllNotes() }
                .doOnComplete { userDao.deleteAll() }
                .doOnComplete { storeAuth.saveToken(null) }
                .doOnComplete { timestampStore.removeTimestamp() }
    }
}

package eu.napcode.gonoteit.repository.user

import android.annotation.SuppressLint

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response

import javax.inject.Inject

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.api.ApolloRxHelper
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.data.notes.NotesLocal
import eu.napcode.gonoteit.model.UserModel
import eu.napcode.gonoteit.utils.TimestampStore
import io.reactivex.Completable
import io.reactivex.Observable

class UserRepositoryImpl @Inject
constructor(
        private val apolloClient: ApolloClient,
        private val storeAuth: StoreAuth,
        private val apolloRxHelper: ApolloRxHelper,
        private val timestampStore: TimestampStore,
        private val notesLocal: NotesLocal) : UserRepository {

    private val isUserLoggedIn: Boolean
        get() {
            val token = storeAuth.token

            return token != null && token.length > 0
        }

    @SuppressLint("CheckResult")
    override fun authenticateUser(login: String, password: String): Observable<Response<AuthenticateMutation.Data>> {
        val authMutation = apolloClient
                .mutate(AuthenticateMutation(login, password))

        return apolloRxHelper.from(authMutation)
                .doOnSubscribe { timestampStore.removeTimestamp() }
    }

    override fun saveUserAuthData(login: String, token: String) {
        storeAuth.saveToken(token)
        storeAuth.saveName(login)
    }

    override fun getLoggedInUser(): UserModel? {

        return if (isUserLoggedIn) {
            UserModel(storeAuth.userName)
        } else null

    }

    override fun logoutUser(): Completable {
        return Completable.fromAction { notesLocal.deleteAllNotes() }
                .doOnComplete { storeAuth.saveToken(null) }
                .doOnComplete { timestampStore.removeTimestamp() }
    }
}

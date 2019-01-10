package eu.napcode.gonoteit.repository.user

import com.apollographql.apollo.api.Response

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.model.UserModel
import io.reactivex.Completable
import io.reactivex.Observable

interface UserRepository {

    fun getLoggedInUser(): UserModel?

    fun authenticateUser(login: String, password: String): Observable<Response<AuthenticateMutation.Data>>

    fun saveUserAuthData(login: String, token: String)

    fun logoutUser(): Completable
}

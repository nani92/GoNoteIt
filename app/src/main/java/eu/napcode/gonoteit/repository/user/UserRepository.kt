package eu.napcode.gonoteit.repository.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.dao.user.UserEntity
import eu.napcode.gonoteit.model.UserModel
import eu.napcode.gonoteit.repository.Resource
import io.reactivex.Completable
import io.reactivex.Observable

interface UserRepository {

    fun isUserLoggedIn(): Boolean

    fun updateUserFromRemote(authResource: MutableLiveData<Resource<AuthenticateMutation.Data>>?)

    fun getLoggedInUser(): LiveData<UserModel?>

    fun authenticateUser(login: String, password: String, authResponse : MutableLiveData<Resource<AuthenticateMutation.Data>>): Observable<Response<AuthenticateMutation.Data>>

    fun logoutUser(): Completable
}

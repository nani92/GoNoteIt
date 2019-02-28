package eu.napcode.gonoteit.ui.login

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.app.utils.isHostValid
import eu.napcode.gonoteit.app.utils.isUserValid
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.repository.user.UserRepository
import eu.napcode.gonoteit.rx.RxSchedulers
import timber.log.Timber

class LoginViewModel @Inject
constructor(private val userRepository: UserRepository, private val rxSchedulers: RxSchedulers) : ViewModel() {

    private var login: String? = null
    private var password: String? = null
    private var host: String? = null

    private val inputsValid = MutableLiveData<Boolean>()
    private val authenticationResource = MutableLiveData<Resource<AuthenticateMutation.Data>>()

    init {
        validate()
    }

    fun areInputsValid(): LiveData<Boolean> {
        return inputsValid
    }

    fun setLogin(login: String) {
        this.login = login
        validate()
    }

    fun setHost(host: String) {
        this.host = host
        validate()
    }

    private fun validate() {
        val valid = isUserValid(login, password) && isHostValid(host)
        Timber.d("Validated input %s and %s. Result: %b", login, password, valid)

        inputsValid.postValue(valid)
    }

    fun setPassword(password: String) {
        this.password = password
        validate()
    }

    @SuppressLint("CheckResult")
    fun login(): MutableLiveData<Resource<AuthenticateMutation.Data>> {
        userRepository
                .authenticateUser(host!!, login!!, password!!, authenticationResource)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .doOnSubscribe { authenticationResource.postValue(Resource.loading<AuthenticateMutation.Data>(null)) }
                .doOnError { error ->
                    authenticationResource.postValue(Resource.error<AuthenticateMutation.Data>(error))
                    Timber.d("Login error: %s", error.localizedMessage)
                }
                .subscribe()

        return authenticationResource
    }

}
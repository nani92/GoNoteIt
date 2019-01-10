package eu.napcode.gonoteit.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import eu.napcode.gonoteit.model.UserModel
import eu.napcode.gonoteit.repository.user.UserRepository
import eu.napcode.gonoteit.rx.RxSchedulers

class MainViewModel @Inject
constructor(private val userRepository: UserRepository, private val rxSchedulers: RxSchedulers) : ViewModel() {
    private val loggedInUser = MutableLiveData<UserModel>()

    fun getLoggedInUser(): MutableLiveData<UserModel> {
        loggedInUser.postValue(userRepository.getLoggedInUser())

        return loggedInUser
    }

    fun logoutUser() {
        userRepository
                .logoutUser()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .doFinally { getLoggedInUser() }
                .subscribe()
    }
}

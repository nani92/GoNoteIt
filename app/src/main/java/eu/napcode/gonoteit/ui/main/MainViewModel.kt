package eu.napcode.gonoteit.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import eu.napcode.gonoteit.model.UserModel
import eu.napcode.gonoteit.repository.user.UserRepository
import eu.napcode.gonoteit.rx.RxSchedulers

class MainViewModel @Inject
constructor(private val userRepository: UserRepository, private val rxSchedulers: RxSchedulers) : ViewModel() {

    fun getLoggedInUser(): LiveData<UserModel?> {
        return userRepository.getLoggedInUser()
    }

    fun logoutUser() {
        userRepository
                .logoutUser()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidMainThread())
                .doFinally { getLoggedInUser() }
                .subscribe()
    }

    fun isUserLoggedIn() : Boolean {
        return userRepository.isUserLoggedIn()
    }
}

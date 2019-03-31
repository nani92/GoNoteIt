package eu.napcode.gonoteit.ui.main

import android.app.Activity
import eu.napcode.gonoteit.R

import eu.napcode.gonoteit.repository.Resource


fun manageProgressBarDisplaying(activity: Activity, status: Resource.Status) {

    if (activity is MainActivity) {
        displayProgressBar(activity, status)
    }
}

private fun displayProgressBar(activity: MainActivity, status: Resource.Status) {

    if (status == Resource.Status.LOADING) {
        activity.showProgressBar()
    } else {
        activity.hideProgressBar()
    }
}

fun showProgressBar(activity: Activity) {

    if (activity is MainActivity) {
        activity.showProgressBar()
    }
}

fun hideProgressBar(activity: Activity) {

    if (activity is MainActivity) {
        activity.hideProgressBar()
    }
}

fun displayMessage(activity: Activity, message: String?) {

    var messageToDisplay : String = if (message == null || message.isEmpty()) {
        activity.getString(R.string.general_error_downloading)
    } else {
        message
    }

    if (activity is MainActivity) {
        activity.displayMessage(messageToDisplay)
    }
}

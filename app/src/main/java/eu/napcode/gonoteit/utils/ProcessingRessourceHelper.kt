package eu.napcode.gonoteit.utils

import android.app.Activity
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.ui.main.displayMessage
import eu.napcode.gonoteit.ui.main.manageProgressBarDisplaying

fun processResource(activity: Activity, resource: Resource<*>) {
    manageProgressBarDisplaying(activity, resource.status)

    if (resource.status == Resource.Status.ERROR) {
        displayMessage(activity, resource.message)
    }
}
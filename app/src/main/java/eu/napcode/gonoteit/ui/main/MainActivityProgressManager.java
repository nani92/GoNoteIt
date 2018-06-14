package eu.napcode.gonoteit.ui.main;

import android.app.Activity;

import eu.napcode.gonoteit.repository.Resource;

public class MainActivityProgressManager {

    public static void manageProgressBarDisplaying(Activity activity, Resource.Status status) {

        if (activity instanceof MainActivity) {
            displayProgressBar((MainActivity) activity, status);
        }
    }

    private static void displayProgressBar(MainActivity activity, Resource.Status status) {

        if (status == Resource.Status.LOADING) {
            activity.showProgressBar();
        } else {
            activity.hideProgressBar();
        }
    }

    public static void showProgressBar(Activity activity) {

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).showProgressBar();
        }
    }

    public static void hideProgressBar(Activity activity) {

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).hideProgressBar();
        }
    }
}

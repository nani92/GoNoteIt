package eu.napcode.gonoteit.utils;

import android.content.Context;

import javax.inject.Inject;

import eu.napcode.gonoteit.R;

public class TrackerUtils {

    private Context context;

    @Inject
    public TrackerUtils(Context context) {
        this.context = context;
    }

    public String getMainScreenName() {
        return context.getString(R.string.tracker_main_screen);
    }

    public String getCreateNoteScreenName() {
        return context.getString(R.string.tracker_create_note_screen);
    }

    public String getCategoryAction() {
        return context.getString(R.string.tracker_category_action);
    }

    public String getActionCreateNote() {
        return context.getString(R.string.tracker_action_create_note);
    }

    public String getLoginScreenName() {
        return context.getString(R.string.tracker_login_screen);
    }
}

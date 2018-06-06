package eu.napcode.gonoteit.utils;

import android.content.Context;

import javax.inject.Inject;

import eu.napcode.gonoteit.R;

public class ErrorMessages {

    private Context context;

    @Inject
    public ErrorMessages(Context context) {
        this.context = context;
    }

    public String getCreatingNoteNotImplementedOfflineMessage() {
        return getNotImplementedOfflineMessage(context.getString(R.string.creating_note));
    }

    public String getDeletingNoteNotImplementedOfflineMessage() {
        return getNotImplementedOfflineMessage(context.getString(R.string.deleting_note));
    }

    public String getUpdatingNoteNotImplementedOfflineMessage() {
        return getNotImplementedOfflineMessage(context.getString(R.string.updating_note));
    }

    public String getOfflineMessage() {
        return context.getString(R.string.youre_offline);
    }

    private String getNotImplementedOfflineMessage(String functionality) {
        return context.getString(R.string.error_not_implemented_offline, functionality);
    }
}

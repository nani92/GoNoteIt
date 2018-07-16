package eu.napcode.gonoteit.ui.create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import eu.napcode.gonoteit.R

class PermissionsDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var view = activity!!.layoutInflater.inflate(R.layout.dialog_privacy, null)

        return AlertDialog.Builder(activity)
                .setTitle(R.string.permissions_dialog_title)
                .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
                .setView(view)
                .create()
    }
}
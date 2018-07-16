package eu.napcode.gonoteit.ui.create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.model.note.NoteModel

class PermissionsDialogFragment : DialogFragment() {

    companion object {
        private val ARG_READ_PERMS = "read perms"
        private val ARG_WRITE_PERMS = "write perms"

        fun newInstance(readPerms: NoteModel.ReadPerms, writePerms: NoteModel.WritePerms): PermissionsDialogFragment {
            val args = Bundle()
            args.putSerializable(ARG_READ_PERMS, readPerms)
            args.putSerializable(ARG_WRITE_PERMS, writePerms)

            val fragment = PermissionsDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var view = activity!!.layoutInflater.inflate(R.layout.dialog_privacy, null)

        return AlertDialog.Builder(activity)
                .setTitle(R.string.permissions_dialog_title)
                .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
                .setView(view)
                .create()
    }
}
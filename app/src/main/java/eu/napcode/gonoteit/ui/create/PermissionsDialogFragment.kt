package eu.napcode.gonoteit.ui.create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.RadioGroup
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.model.note.NoteModel
import eu.napcode.gonoteit.model.note.NoteModel.ReadPerms
import eu.napcode.gonoteit.model.note.NoteModel.ReadPerms.*
import eu.napcode.gonoteit.model.note.NoteModel.WritePerms
import eu.napcode.gonoteit.model.note.NoteModel.WritePerms.EVERYONE
import eu.napcode.gonoteit.model.note.NoteModel.WritePerms.ONLY_OWNER

class PermissionsDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_READ_PERMS = "read perms"
        private const val ARG_WRITE_PERMS = "write perms"

        fun newInstance(readPerms: ReadPerms, writePerms: WritePerms): PermissionsDialogFragment {
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
        displayReadPerms(view)
        displayWritePerms(view)

        return AlertDialog.Builder(activity)
                .setTitle(R.string.permissions_dialog_title)
                .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
                .setView(view)
                .create()
    }

    private fun displayReadPerms(view: View) {
        var readRadioGroup = view.findViewById<RadioGroup>(R.id.readPermsRadioGroup)

        when (getReadPerms()) {
            PUBLIC -> readRadioGroup.check(R.id.read_public_radio_button)
            PRIVATE -> readRadioGroup.check(R.id.read_private_radio_button)
            VIA_LINK -> readRadioGroup.check(R.id.read_via_link_radio_button)
        }
    }

    private fun getReadPerms() = arguments!!.getSerializable(ARG_READ_PERMS) as ReadPerms

    private fun displayWritePerms(view: View) {
        var readRadioGroup = view.findViewById<RadioGroup>(R.id.writePermsRadioGroup)

        when (getWritePerms()) {
            EVERYONE -> readRadioGroup.check(R.id.write_everyone_radio_button)
            ONLY_OWNER -> readRadioGroup.check(R.id.write_only_owner_radio_button)
        }
    }

    private fun getWritePerms() = arguments!!.getSerializable(ARG_WRITE_PERMS) as WritePerms
}
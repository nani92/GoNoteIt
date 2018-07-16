package eu.napcode.gonoteit.ui.create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import eu.napcode.gonoteit.R
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
        readRadioGroup.setOnCheckedChangeListener { _, _ -> displayReadExplanation(view)}

        when (getReadPerms()) {
            PUBLIC -> readRadioGroup.check(R.id.read_public_radio_button)
            PRIVATE -> readRadioGroup.check(R.id.read_private_radio_button)
            VIA_LINK -> readRadioGroup.check(R.id.read_via_link_radio_button)
        }
    }

    private fun getReadPerms() = arguments!!.getSerializable(ARG_READ_PERMS) as ReadPerms

    private fun displayWritePerms(view: View) {
        var writeRadioGroup = view.findViewById<RadioGroup>(R.id.writePermsRadioGroup)
        writeRadioGroup.setOnCheckedChangeListener { _, _ -> displayWriteExplanation(view)}

        when (getWritePerms()) {
            EVERYONE -> writeRadioGroup.check(R.id.write_everyone_radio_button)
            ONLY_OWNER -> writeRadioGroup.check(R.id.write_only_owner_radio_button)
        }
    }

    private fun getWritePerms() = arguments!!.getSerializable(ARG_WRITE_PERMS) as WritePerms

    private fun displayReadExplanation(view: View) {
        var readRadioGroup = view.findViewById<RadioGroup>(R.id.readPermsRadioGroup)

        var permsText = when (readRadioGroup.checkedRadioButtonId) {
            R.id.read_public_radio_button -> getString(R.string.perms_read_explanation_everyone)
            R.id.read_private_radio_button -> getString(R.string.perms_read_explanation_owner)
            else -> getString(R.string.perms_read_explanation_link)
        }

        view.findViewById<TextView>(R.id.readPermsExplanationTextView).text =
                getString(R.string.perms_read_explanation, permsText)
    }

    private fun displayWriteExplanation(view: View) {
        var readRadioGroup = view.findViewById<RadioGroup>(R.id.writePermsRadioGroup)

        var permsText = when (readRadioGroup.checkedRadioButtonId) {
            R.id.write_everyone_radio_button -> getString(R.string.perms_write_explanation_everyone)
            else -> getString(R.string.perms_write_explanation_owner)
        }

        view.findViewById<TextView>(R.id.writePermsExplanationTextView).text =
                getString(R.string.perms_write_explanation, permsText)
    }
}
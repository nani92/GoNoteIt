package eu.napcode.gonoteit.ui.create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.type.Access

class PermissionsDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_READ_PERMS = "read perms"
        private const val ARG_WRITE_PERMS = "write perms"

        fun newInstance(readPerms: Access, writePerms: Access): PermissionsDialogFragment {
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
        var listener = activity as PermissionsDialogListener

        return AlertDialog.Builder(activity)
                .setTitle(R.string.permissions_dialog_title)
                .setPositiveButton(R.string.ok) { _, _ ->
                    run {
                        listener.onChangedReadPerms(getChosenReadPerm(view))
                        listener.onChangedWritePerms(getChosenWritePerm(view))
                        dismiss()
                    }
                }
                .setView(view)
                .create()
    }

    private fun displayReadPerms(view: View) {
        var readRadioGroup = view.findViewById<RadioGroup>(R.id.readPermsRadioGroup)
        readRadioGroup.setOnCheckedChangeListener { _, _ -> displayReadExplanation(view) }

        when (getReadPerms()) {
            Access.PUBLIC -> readRadioGroup.check(R.id.read_public_radio_button)
            Access.PRIVATE -> readRadioGroup.check(R.id.read_private_radio_button)
            Access.INTERNAL -> readRadioGroup.check(R.id.read_via_link_radio_button)
        }
    }

    private fun getReadPerms() = arguments!!.getSerializable(ARG_READ_PERMS) as Access

    private fun displayWritePerms(view: View) {
        var writeRadioGroup = view.findViewById<RadioGroup>(R.id.writePermsRadioGroup)
        writeRadioGroup.setOnCheckedChangeListener { _, _ -> displayWriteExplanation(view) }

        when (getWritePerms()) {
            Access.PUBLIC -> writeRadioGroup.check(R.id.write_everyone_radio_button)
            Access.PRIVATE -> writeRadioGroup.check(R.id.write_only_owner_radio_button)
            Access.INTERNAL -> writeRadioGroup.check(R.id.write_group_radio_button)
        }
    }

    private fun getWritePerms() = arguments!!.getSerializable(ARG_WRITE_PERMS) as Access

    private fun displayReadExplanation(view: View) {
        view.findViewById<TextView>(R.id.readPermsExplanationTextView).text =
                        getReadPermsExplanation(context!!, getChosenReadPerm(view))
    }

    private fun displayWriteExplanation(view: View) {

        view.findViewById<TextView>(R.id.writePermsExplanationTextView).text =
                        getWritePermsExplanation(context!!, getChosenWritePerm(view))
    }

    private fun getChosenReadPerm(view: View): Access {
        var readRadioGroup = view.findViewById<RadioGroup>(R.id.readPermsRadioGroup)

        return when (readRadioGroup.checkedRadioButtonId) {
            R.id.read_public_radio_button -> Access.PUBLIC
            R.id.read_via_link_radio_button -> Access.INTERNAL
            else -> Access.PRIVATE
        }
    }

    private fun getChosenWritePerm(view: View): Access {
        var writeRadioGroup = view.findViewById<RadioGroup>(R.id.writePermsRadioGroup)

        return when (writeRadioGroup.checkedRadioButtonId) {
            R.id.write_everyone_radio_button -> Access.INTERNAL
            R.id.write_group_radio_button -> Access.INTERNAL
            else -> Access.INTERNAL
        }
    }

    interface PermissionsDialogListener {

        fun onChangedReadPerms(readAccess: Access)

        fun onChangedWritePerms(writeAccess: Access)
    }
}


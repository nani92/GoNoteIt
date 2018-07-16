package eu.napcode.gonoteit.ui.create

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import eu.napcode.gonoteit.R

class PermisionsDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view =  activity!!.layoutInflater.inflate(R.layout.dialog_privacy, null)

        return super.onCreateDialog(savedInstanceState)
    }
}
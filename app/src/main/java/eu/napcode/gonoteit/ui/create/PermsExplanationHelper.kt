package eu.napcode.gonoteit.ui.create

import android.content.Context
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.type.Access

fun getReadPermsExplanation(context: Context, readAccess: Access) : String {

    var access: String =  when(readAccess) {
        Access.PUBLIC -> context.resources.getString(R.string.perms_read_explanation_everyone)
        Access.PRIVATE -> context.resources.getString(R.string.perms_read_explanation_link)
        Access.INTERNAL -> context.resources.getString(R.string.perms_read_explanation_group)
        else -> context.resources.getString(R.string.perms_read_explanation_owner)
    }

    return context.getString(R.string.perms_read_explanation, access)
}

fun getWritePermsExplanation(context: Context, writeAccess: Access) : String {

    var access: String =  when(writeAccess) {
        Access.PUBLIC -> context.resources.getString(R.string.perms_write_explanation_everyone)
        Access.INTERNAL -> context.resources.getString(R.string.perms_write_explanation_group)
        else -> context.resources.getString(R.string.perms_write_explanation_owner)
    }

    return context.getString(R.string.perms_write_explanation, access)
}
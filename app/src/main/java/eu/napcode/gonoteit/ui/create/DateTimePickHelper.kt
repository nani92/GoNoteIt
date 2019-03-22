package eu.napcode.gonoteit.ui.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

class DateTimePickHelper(var context: Context, val listener: DateTimeSetListener) {

    var calendar = Calendar.getInstance()

    fun startPicking() {
        calendar = Calendar.getInstance()
        displayDatePicker()
    }

    private fun displayDatePicker() {
        DatePickerDialog(
                context,
                getDatePickerListener(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getDatePickerListener(): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            displayTimePicker()
        }
    }

    private fun displayTimePicker() {
        TimePickerDialog(
                context,
                getTimePickerListener(),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show()
    }

    private fun getTimePickerListener(): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            listener.onSetDateTime(calendar)
        }
    }
}

public interface DateTimeSetListener {
    fun onSetDateTime(calendar: Calendar)
}
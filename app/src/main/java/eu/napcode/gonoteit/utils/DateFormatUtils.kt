package eu.napcode.gonoteit.utils

import java.text.SimpleDateFormat
import java.util.*

public var dateFormatWithTime =
        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

public var dateFormat =
        SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault())


public var timeFormat =
        SimpleDateFormat("HH:mm", Locale.getDefault())

public fun getTimestampLong(timestamp: Long?) : Long? {

    if (timestamp == null) {
        return null
    }

    return timestamp * 1000
}

public fun getTimestampShort(timestamp: Long?) : Long? {

    if (timestamp == null) {
        return null
    }

    return timestamp / 1000
}

public fun isSameDate(cal: Calendar, cal2: Calendar) : Boolean {
    return cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
}

fun getTodayCalendar() : Calendar {
    val todayCalendar = Calendar.getInstance()
    todayCalendar.time = Date()

    return todayCalendar
}

fun getTomorrowCalendar() : Calendar {
    val tomorrowCalendar = Calendar.getInstance()
    tomorrowCalendar.time = Date()
    tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1)

    return tomorrowCalendar
}

fun getDayAfterTomorrowCalendar() : Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DAY_OF_YEAR, 2)

    return calendar
}

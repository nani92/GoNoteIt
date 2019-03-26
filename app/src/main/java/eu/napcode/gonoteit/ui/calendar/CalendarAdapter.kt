package eu.napcode.gonoteit.ui.calendar

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.utils.dateFormat
import eu.napcode.gonoteit.utils.timeFormat
import kotlinx.android.synthetic.main.item_calendar_event.view.*
import kotlinx.android.synthetic.main.item_date.view.*
import java.util.*

class CalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var calendarElements: List<CalendarAdapterElement> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LAYOUT_DATE) {
            DateViewHolder(parent.inflate(R.layout.item_date))
        } else {
            EventViewHolder(parent.inflate(R.layout.item_calendar_event))
        }
    }

    override fun getItemCount() = calendarElements.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == LAYOUT_DATE) {
            (holder as DateViewHolder).bind(position)
        } else {
            (holder as EventViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (calendarElements[position].isDate) {
            LAYOUT_DATE
        } else {
            LAYOUT_EVENT
        }
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val date = dateFormat.format(calendarElements[position].date!!.time)
            itemView.headerTextView.text = date
        }
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val note = calendarElements[position].note!!
            itemView.eventTitleTextView.text = note.title
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = note.date!! * 1000
            itemView.eventHourTextView.text = timeFormat.format(calendar.time)
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    companion object {
        const val LAYOUT_DATE = 100
        const val LAYOUT_EVENT = 101
    }
}
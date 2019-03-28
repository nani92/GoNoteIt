package eu.napcode.gonoteit.ui.calendar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.utils.dateFormat
import eu.napcode.gonoteit.utils.isSameDate
import eu.napcode.gonoteit.utils.timeFormat
import kotlinx.android.synthetic.main.item_calendar_event.view.*
import kotlinx.android.synthetic.main.item_date.view.*
import java.util.*

class CalendarAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            val today = Calendar.getInstance()
            today.time = Date()

            if(isSameDate(calendarElements[position].date!!, today)) {
                val todayString = context.getString(R.string.today)
                val displayText = "$todayString  $date"
                itemView.headerTextView.text = displayText

                itemView.headerTextView.background = ColorDrawable(ContextCompat.getColor(context, R.color.colorAccent))
            } else {
                itemView.headerTextView.text = date
            }
        }
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {

            if (calendarElements[position].note == null) {
                itemView.eventTitleTextView.visibility = GONE
                itemView.eventHourTextView.visibility = GONE
                itemView.divider.visibility = GONE
                itemView.noEvents.visibility = VISIBLE

                return
            }

            if (calendarElements[position - 1].isDate) {
                itemView.divider.visibility = GONE
            } else {
                itemView.divider.visibility = VISIBLE
            }

            val note = calendarElements[position].note!!
            itemView.eventTitleTextView.visibility = VISIBLE
            itemView.eventHourTextView.visibility = VISIBLE
            itemView.noEvents.visibility = GONE

            itemView.eventTitleTextView.text = note.title
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = note.date!!
            itemView.eventHourTextView.text = timeFormat.format(calendar.time)
            itemView.eventHourTextView.setPadding(0, 0, 0, 0)
            itemView.eventTitleTextView.setPadding(0, 0, 0, 0)

            if (position == calendarElements.size - 1
                    || calendarElements[position + 1].isDate) {
                val padding = context.resources.getDimension(R.dimen.base_margin)
                itemView.eventHourTextView.setPadding(0, 0, 0, padding.toInt())
                itemView.eventTitleTextView.setPadding(0, 0, 0, padding.toInt())
            }
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
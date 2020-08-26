package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.BirthdayDto
import com.datepollsystems.datepoll.data.Booking
import com.datepollsystems.datepoll.data.Event
import java.util.*

class CardAdapter(val activity: AppCompatActivity) :
    RecyclerView.Adapter<CardViewHolder>() {
    private val birthday = activity.getString(R.string.birthday)
    private val bookings = activity.getString(R.string.bookings)
    private val events = activity.getString(R.string.events)

    var data = listOf(birthday, bookings)

    var birthdayData: LinkedList<BirthdayDto> = LinkedList()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    var eventsData: LinkedList<Event> = LinkedList()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    var bookingsData: LinkedList<Booking> = LinkedList()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        when(data[position]){
            birthday -> {
                val adapter =
                    OldBithdayAdapter()
                adapter.data = birthdayData
                holder.tvHeadline.text = birthday
                holder.recyclerView.adapter = adapter
                if(birthdayData.isEmpty())
                    holder.view.visibility = View.INVISIBLE
            }

            bookings -> {

                val adapter =
                    BookingAdapter()
                adapter.data = bookingsData
                holder.tvHeadline.text = bookings
                holder.recyclerView.adapter = adapter
                if(bookingsData.isEmpty())
                    holder.view.visibility = View.INVISIBLE
            }

            events -> {
                val adapter =
                    EventAdapter(
                        activity
                    )
                adapter.data = eventsData
                holder.tvHeadline.text = events
                holder.recyclerView.adapter = adapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CardViewHolder(
            layoutInflater.inflate(R.layout.card_item, parent, false)
        )
    }
}

class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvHeadline: TextView = view.findViewById(R.id.tvHeadline)
    val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    var inflate = true
}

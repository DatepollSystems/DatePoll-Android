package com.datepollsystems.datepoll.ui.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Instance

class InstanceOptionsAdapter(
    val sheet: InstanceOptionsBottomSheetDialog,
    val response: MutableLiveData<Instance>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<Instance>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InstanceOptionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.decision_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as InstanceOptionViewHolder
        val i = data[position]

        h.text.text = i.name
        h.line.setOnClickListener {
            response.postValue(i)
            sheet.dismiss()
        }
    }
}

class InstanceOptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.textView)
    val line: LinearLayout = view.findViewById(R.id.line)
}
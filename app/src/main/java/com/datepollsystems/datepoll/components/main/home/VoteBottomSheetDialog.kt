package com.datepollsystems.datepoll.components.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDbModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.vote_bottom_sheet.view.*

class VoteBottomSheetDialog(val list: List<EventDecisionDbModel>, val ld: MutableLiveData<EventDecisionDbModel>): BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val v = inflater.inflate(R.layout.vote_bottom_sheet, container, false)
        val adapter =
            VoteOptionAdapter(
                sheet = this,
                response = ld
            )
        v.vote_options.adapter = adapter
        adapter.data = list
        return v
    }
}
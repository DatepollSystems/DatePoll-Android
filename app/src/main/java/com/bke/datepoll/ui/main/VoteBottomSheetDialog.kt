package com.bke.datepoll.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.R
import com.bke.datepoll.data.Decision
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.vote_bottom_sheet.view.*

class VoteBottomSheetDialog(val list: List<Decision>, val ld: MutableLiveData<Decision>): BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val v = inflater.inflate(R.layout.vote_bottom_sheet, container, false)
        val adapter = VoteOptionAdapter(sheet = this, liveData = ld)
        v.vote_options.adapter = adapter
        adapter.data = list
        return v
    }
}
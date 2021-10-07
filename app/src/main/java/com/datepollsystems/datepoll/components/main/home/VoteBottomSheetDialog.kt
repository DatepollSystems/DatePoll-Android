package com.datepollsystems.datepoll.components.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDbModel
import com.datepollsystems.datepoll.databinding.VoteBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VoteBottomSheetDialog(val list: List<EventDecisionDbModel>, val ld: MutableLiveData<EventDecisionDbModel>): BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = VoteBottomSheetBinding.inflate(layoutInflater)

        val adapter =
            VoteOptionAdapter(
                sheet = this,
                response = ld
            )
        binding.voteOptions.adapter = adapter
        adapter.data = list
        return binding.root
    }
}
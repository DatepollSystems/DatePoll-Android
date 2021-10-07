package com.datepollsystems.datepoll.components.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.Instance
import com.datepollsystems.datepoll.databinding.ChooseInstanceBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InstanceOptionsBottomSheetDialog(val list: List<Instance>, val ld: MutableLiveData<Instance>): BottomSheetDialogFragment(){

    private var _binding: ChooseInstanceBottomSheetBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = ChooseInstanceBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = InstanceOptionsAdapter(sheet = this, response = ld)
        binding.options.adapter = adapter
        adapter.data = list
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
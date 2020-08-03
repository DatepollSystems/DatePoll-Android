package com.datepollsystems.datepoll.components.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Instance
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.choose_instance_bottom_sheet.view.*


class InstanceOptionsBottomSheetDialog(val list: List<Instance>, val ld: MutableLiveData<Instance>): BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val v = inflater.inflate(R.layout.choose_instance_bottom_sheet, container, false)
        val adapter = InstanceOptionsAdapter(sheet = this, response = ld)
        v.options.adapter = adapter
        adapter.data = list
        return v
    }
}
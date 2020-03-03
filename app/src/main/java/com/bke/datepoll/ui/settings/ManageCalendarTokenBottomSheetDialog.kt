package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bke.datepoll.R
import com.bke.datepoll.vm.SettingsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.settings_manage_cal_token_bottom_sheet.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ManageCalendarTokenBottomSheetDialog(val data: MutableLiveData<String>): BottomSheetDialogFragment(){

    val vm: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.settings_manage_cal_token_bottom_sheet, container, false)

        v.btnClose.setOnClickListener {
            this.dismiss()
        }

        v.btnResetToken.setOnClickListener {
            //TODO

            vm.resetCalendarToken()
        }

        data.observe(viewLifecycleOwner, Observer {
            it?.let {
                v.tiCalendarToken.editText?.setText(it)
            }
        })

        return v
    }
}
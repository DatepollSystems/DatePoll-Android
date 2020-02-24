package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bke.datepoll.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.settings_manage_cal_token_bottom_sheet.view.*

class ManageCalendarTokenBottomSheetDialog: BottomSheetDialogFragment(){

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
        }

        return v
    }
}
package com.datepollsystems.datepoll.components.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.SettingsManageCalTokenBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ManageCalendarTokenBottomSheetDialog(val data: MutableLiveData<String>): BottomSheetDialogFragment(){

    val vm: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SettingsManageCalTokenBottomSheetBinding.inflate(layoutInflater)

        binding.btnClose.setOnClickListener {
            this.dismiss()
        }

        binding.btnResetToken.setOnClickListener {
            //TODO

            vm.resetCalendarToken()
        }

        data.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tiCalendarToken.editText?.setText(it)
            }
        })

        return binding.root
    }
}
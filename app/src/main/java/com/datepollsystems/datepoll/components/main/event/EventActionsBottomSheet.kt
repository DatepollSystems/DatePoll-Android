package com.datepollsystems.datepoll.components.main.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel
import com.datepollsystems.datepoll.databinding.EventActionsBottomSheetBinding
import com.datepollsystems.datepoll.utils.ClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EventActionsBottomSheet(val e: EventDbModel) : BottomSheetDialogFragment() {

    val vm: EventViewModel by sharedViewModel()

    private var _binding: EventActionsBottomSheetBinding? = null
    val binding: EventActionsBottomSheetBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventActionsBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickListener = ClickListener {
            vm.removeEventAnswer(e.id)
            dismiss()
        }

        return binding.root
    }
}
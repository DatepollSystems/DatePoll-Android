package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bke.datepoll.R
import kotlinx.android.synthetic.main.fragment_settings_change_password.*
import kotlinx.android.synthetic.main.fragment_settings_change_password.view.*

class SettingsChangePasswordFragment : Fragment() {

    enum class Step { ONE, TWO, THREE }

    var currentStep: Step = Step.ONE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_settings_change_password, container, false)

        view.btnConfirmOldPasswd.setOnClickListener {
            changeCurrentStep(true)
        }

        return view
    }

    override fun onStart() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (currentStep) {
                        Step.ONE -> findNavController().popBackStack()
                        Step.TWO -> changeCurrentStep(false)
                        Step.THREE -> changeCurrentStep(false)
                    }
                }
            })

        super.onStart()
    }

    /**
     * @param direction true -> next step; false -> one step back
     */
    private fun changeCurrentStep(direction: Boolean) {
        when (currentStep) {
            Step.ONE -> {
                if (direction) {

                    //TODO check if old password is valid

                    currentStep = Step.TWO
                    tiOldPassword.visibility = View.GONE
                    btnConfirmOldPasswd.visibility = View.GONE

                    tiNewPassword.visibility = View.VISIBLE
                    tiConfirmNewPassword.visibility = View.VISIBLE
                    btnConfirmNewPassword.visibility = View.VISIBLE
                } else {
                    Log.i(tag, "First step, can't go back anymore")
                }
            }
            Step.TWO -> {
                if (direction) {
                    currentStep = Step.THREE


                } else {
                    currentStep = Step.ONE
                    tiOldPassword.visibility = View.VISIBLE
                    btnConfirmOldPasswd.visibility = View.VISIBLE
                }
            }
            Step.THREE -> {

            }

        }
    }

}


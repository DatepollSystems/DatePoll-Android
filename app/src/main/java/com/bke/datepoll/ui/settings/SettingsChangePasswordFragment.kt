package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bke.datepoll.R
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.vm.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings_change_password.*
import kotlinx.android.synthetic.main.fragment_settings_change_password.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsChangePasswordFragment : Fragment() {

    enum class Step { ONE, TWO, THREE }

    var currentStep: Step = Step.ONE

    lateinit var currentView: View

    val vm: SettingsViewModel by sharedViewModel()

    private var oldPass: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_settings_change_password, container, false)

        view.btnConfirmOldPasswd.setOnClickListener {
            val p = view.tiOldPassword.editText?.text.toString()
            if (p.isNotBlank()) {
                oldPass = p
                vm.checkPassword(p)
            } else {
                tiOldPassword.error = getString(R.string.field_is_empty)
            }
        }

        view.btnConfirmNewPassword.setOnClickListener {
            val p1 = view.tiNewPassword.editText?.text.toString()
            val p2 = view.tiConfirmNewPassword.editText?.text.toString()

            var quit = false

            if (!p1.isNotBlank()) {
                quit = true
                view.tiNewPassword.error = getString(R.string.field_is_empty)
            }


            if (!p2.isNotBlank()) {
                quit = true
                view.tiConfirmNewPassword.error = getString(R.string.field_is_empty)
            }

            if (!quit) {
                if (p1 == p2) {
                    if (p1.length >= 6)
                        oldPass?.let {
                            view.tiConfirmNewPassword.error = ""
                            view.tiNewPassword.error = ""
                            vm.changePassword(it, p1)
                        }
                    else {
                        view.tiNewPassword.error = "Password must have min. 6 characters"
                    }
                } else {
                    view.tiNewPassword.error = getString(R.string.passwords_dont_match)
                    view.tiConfirmNewPassword.error = getString(R.string.passwords_dont_match)
                }
            }
        }

        view.tiOldPassword.editText?.addTextChangedListener {
            if (!it.toString().isNotBlank()) {
                view.tiOldPassword.error = getString(R.string.field_is_empty)
            } else {
                view.tiOldPassword.error = ""
            }
        }

        currentView = view
        return view
    }

    override fun onStart() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (currentStep) {
                        Step.ONE -> findNavController().popBackStack()
                        Step.TWO -> changeCurrentStep(currentView, false)
                        Step.THREE -> findNavController().popBackStack()
                    }
                }
            })

        refresh.isEnabled = false

        vm.checkPasswordState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.DONE -> {
                        changeCurrentStep(currentView, true)
                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                    }

                    ENetworkState.ERROR -> {
                        Log.e(tag, "Something went wrong while validating old password")
                        Snackbar.make(
                            currentView,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()

                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                    }
                    ENetworkState.LOADING -> {
                        refresh.isEnabled = true
                        refresh.isRefreshing = true
                    }
                }
            }
        })

        vm.changePasswordState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.DONE -> {
                        changeCurrentStep(currentView, true)
                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                    }
                    ENetworkState.ERROR -> {
                        Log.e(tag, "Something went wrong while validating old password")
                        Snackbar.make(
                            currentView,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()

                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                    }
                    ENetworkState.LOADING -> {
                        refresh.isEnabled = true
                        refresh.isRefreshing = true
                    }
                }
            }
        })

        super.onStart()
    }


    /**
     * @param direction true -> next step; false -> one step back
     */
    private fun changeCurrentStep(view: View, direction: Boolean) {
        when (currentStep) {
            Step.ONE -> {
                if (direction) {
                    currentStep = Step.TWO
                    tiOldPassword.visibility = View.GONE
                    btnConfirmOldPasswd.visibility = View.GONE
                    tvOne.visibility = View.INVISIBLE

                    ivCheck1.visibility = View.VISIBLE
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


                    ivCheck2.visibility = View.VISIBLE
                    tvTwo.visibility = View.INVISIBLE
                    tiNewPassword.visibility = View.GONE
                    tiConfirmNewPassword.visibility = View.GONE
                    btnConfirmNewPassword.visibility = View.GONE

                    Snackbar.make(view, "Password update successful", Snackbar.LENGTH_SHORT).show()
                } else {
                    currentStep = Step.ONE
                    tiOldPassword.visibility = View.VISIBLE
                    btnConfirmOldPasswd.visibility = View.VISIBLE
                    tvOne.visibility = View.VISIBLE

                    ivCheck1.visibility = View.INVISIBLE
                    tiNewPassword.visibility = View.GONE
                    tiConfirmNewPassword.visibility = View.GONE
                    btnConfirmNewPassword.visibility = View.GONE
                }
            }

            Step.THREE -> {
                if (direction) {
                    Log.i(tag, "Last step reached, can't go further")
                    Thread.sleep(2000)
                    onStop()
                    onDestroy()
                    findNavController().popBackStack()
                } else {
                    currentStep = Step.TWO
                    ivCheck2.visibility = View.INVISIBLE
                    tvTwo.visibility = View.VISIBLE
                    tiConfirmNewPassword.visibility = View.VISIBLE
                    tiNewPassword.visibility = View.VISIBLE
                    btnConfirmNewPassword.visibility = View.VISIBLE
                }
            }

        }
    }

}


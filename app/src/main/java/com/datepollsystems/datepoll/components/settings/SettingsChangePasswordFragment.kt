package com.datepollsystems.datepoll.components.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsChangePasswordBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings_change_password.*
import kotlinx.android.synthetic.main.fragment_settings_change_password.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

enum class EStep { ONE, TWO, THREE }

class SettingsChangePasswordFragment : Fragment() {

    lateinit var currentView: View

    val vm: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSettingsChangePasswordBinding>(
            inflater, R.layout.fragment_settings_change_password, container, false
        )

        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner

        val view = binding.root

        view.btnConfirmOldPasswd.setOnClickListener {
            val p = vm.changePasswordOldPass.value!!
            if (p.isNotBlank()) {
                vm.checkPassword(p)
            } else {
                tiOldPassword.error = getString(R.string.field_is_empty)
            }
        }

        view.btnConfirmNewPassword.setOnClickListener {
            val p1 = vm.changePasswordNewPass.value!!
            val p2 = vm.changePasswordConfirmNewPass.value!!

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
                        vm.changePasswordOldPass.value?.let {
                            view.tiConfirmNewPassword.error = ""
                            view.tiNewPassword.error = ""
                            vm.changePassword(it, p1)
                        }
                    else {
                        view.tiNewPassword.error = getString(R.string.password_min_six_char)
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

    private fun resetViewAndGoBack(){
        findNavController().popBackStack()
        currentView.ivCheck1.visibility = View.INVISIBLE
        currentView.ivCheck2.visibility = View.INVISIBLE
        currentView.tvOne.visibility = View.VISIBLE
        currentView.tvTwo.visibility = View.VISIBLE
        currentView.tiOldPassword.visibility = View.VISIBLE
        vm.changePasswordStep.value = EStep.ONE
        vm.changePasswordOldPass.value = ""
        vm.changePasswordNewPass.value = ""
        vm.changePasswordConfirmNewPass.value = ""
    }

    override fun onStart() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (vm.changePasswordStep.value) {
                        EStep.ONE -> resetViewAndGoBack()
                        EStep.TWO -> changeCurrentStep(currentView, false)
                        EStep.THREE -> resetViewAndGoBack()
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
                        Timber.e("Something went wrong while validating old password")
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

                vm.checkPasswordState.postValue(null)
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
                        Timber.e("Something went wrong while validating old password")
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

                vm.changePasswordState.postValue(null)
            }
        })

        super.onStart()
    }


    /**
     * @param direction true -> next step; false -> one step back
     */
    private fun changeCurrentStep(view: View, direction: Boolean) {
        when (vm.changePasswordStep.value) {
            EStep.ONE -> {
                if (direction) {
                    vm.changePasswordStep.value = EStep.TWO
                    tiOldPassword.visibility = View.GONE
                    btnConfirmOldPasswd.visibility = View.GONE
                    tvOne.visibility = View.INVISIBLE

                    ivCheck1.visibility = View.VISIBLE
                    tiNewPassword.visibility = View.VISIBLE
                    tiConfirmNewPassword.visibility = View.VISIBLE
                    btnConfirmNewPassword.visibility = View.VISIBLE
                } else {
                    Timber.i("First step, can't go back anymore")
                }
            }

            EStep.TWO -> {
                if (direction) {

                    vm.changePasswordOldPass.postValue("")
                    vm.changePasswordNewPass.postValue("")
                    vm.changePasswordConfirmNewPass.postValue("")

                    ivCheck2.visibility = View.VISIBLE
                    tvTwo.visibility = View.INVISIBLE
                    tiNewPassword.visibility = View.GONE
                    tiConfirmNewPassword.visibility = View.GONE
                    btnConfirmNewPassword.visibility = View.GONE
                    Snackbar.make(view, "Password update successful", Snackbar.LENGTH_SHORT).show()
                    vm.changePasswordStep.value = EStep.THREE
                } else {
                    vm.changePasswordStep.value = EStep.ONE
                    tiOldPassword.visibility = View.VISIBLE
                    btnConfirmOldPasswd.visibility = View.VISIBLE
                    tvOne.visibility = View.VISIBLE

                    ivCheck1.visibility = View.INVISIBLE
                    tiNewPassword.visibility = View.GONE
                    tiConfirmNewPassword.visibility = View.GONE
                    btnConfirmNewPassword.visibility = View.GONE
                }
            }

            EStep.THREE -> {
                if (direction) {
                    Timber.i("Last step reached, can't go further")
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(1500)
                        findNavController().popBackStack()

                    }
                } else {
                    vm.changePasswordStep.value = EStep.TWO
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


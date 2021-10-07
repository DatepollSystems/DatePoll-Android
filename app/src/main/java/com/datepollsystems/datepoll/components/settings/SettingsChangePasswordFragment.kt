package com.datepollsystems.datepoll.components.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.databinding.FragmentSettingsChangePasswordBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

enum class EStep { ONE, TWO, THREE }

class SettingsChangePasswordFragment : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()

    private lateinit var binding: FragmentSettingsChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsChangePasswordBinding.inflate(layoutInflater)

        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner

        val view = binding.root

        binding.btnConfirmOldPasswd.setOnClickListener {
            val p = vm.changePasswordOldPass.value!!
            if (p.isNotBlank()) {
                vm.checkPassword(p)
            } else {
                binding.tiOldPassword.error = getString(R.string.field_is_empty)
            }
        }

        binding.btnConfirmNewPassword.setOnClickListener {
            val p1 = vm.changePasswordNewPass.value!!
            val p2 = vm.changePasswordConfirmNewPass.value!!

            var quit = false

            if (!p1.isNotBlank()) {
                quit = true
                binding.tiNewPassword.error = getString(R.string.field_is_empty)
            }


            if (!p2.isNotBlank()) {
                quit = true
                binding.tiConfirmNewPassword.error = getString(R.string.field_is_empty)
            }

            if (!quit) {
                if (p1 == p2) {
                    if (p1.length >= 6)
                        vm.changePasswordOldPass.value?.let {
                            binding.tiConfirmNewPassword.error = ""
                            binding.tiNewPassword.error = ""
                            vm.changePassword(it, p1)
                        }
                    else {
                        binding.tiNewPassword.error = getString(R.string.password_min_six_char)
                    }
                } else {
                    binding.tiNewPassword.error = getString(R.string.passwords_dont_match)
                    binding.tiConfirmNewPassword.error = getString(R.string.passwords_dont_match)
                }
            }
        }


        binding.tiOldPassword.editText?.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.tiOldPassword.error = getString(R.string.field_is_empty)
            } else {
                binding.tiOldPassword.error = ""
            }
        }

        return view
    }

    private fun resetViewAndGoBack(){
        findNavController().popBackStack()
        binding.ivCheck1.visibility = View.INVISIBLE
        binding.ivCheck2.visibility = View.INVISIBLE
        binding.tvOne.visibility = View.VISIBLE
        binding.tvTwo.visibility = View.VISIBLE
        binding.tiOldPassword.visibility = View.VISIBLE
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
                        EStep.TWO -> changeCurrentStep(binding.root, false)
                        EStep.THREE -> resetViewAndGoBack()
                    }
                }
            })

        binding.refresh.isEnabled = false

        vm.checkPasswordState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.DONE -> {
                        changeCurrentStep(binding.root, true)
                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
                    }

                    ENetworkState.ERROR -> {
                        Timber.e("Something went wrong while validating old password")
                        Snackbar.make(
                            binding.root,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()

                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
                    }
                    ENetworkState.LOADING -> {
                        binding.refresh.isEnabled = true
                        binding.refresh.isRefreshing = true
                    }
                }

                vm.checkPasswordState.postValue(null)
            }
        })

        vm.changePasswordState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.DONE -> {
                        changeCurrentStep(binding.root, true)
                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
                    }
                    ENetworkState.ERROR -> {
                        Timber.e("Something went wrong while validating old password")
                        Snackbar.make(
                            binding.root,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()

                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
                    }
                    ENetworkState.LOADING -> {
                        binding.refresh.isEnabled = true
                        binding.refresh.isRefreshing = true
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
                    binding.tiOldPassword.visibility = View.GONE
                    binding.btnConfirmOldPasswd.visibility = View.GONE
                    binding.tvOne.visibility = View.INVISIBLE

                    binding.ivCheck1.visibility = View.VISIBLE
                    binding.tiNewPassword.visibility = View.VISIBLE
                    binding.tiConfirmNewPassword.visibility = View.VISIBLE
                    binding.btnConfirmNewPassword.visibility = View.VISIBLE
                } else {
                    Timber.i("First step, can't go back anymore")
                }
            }

            EStep.TWO -> {
                if (direction) {

                    vm.changePasswordOldPass.postValue("")
                    vm.changePasswordNewPass.postValue("")
                    vm.changePasswordConfirmNewPass.postValue("")

                    binding.ivCheck2.visibility = View.VISIBLE
                    binding.tvTwo.visibility = View.INVISIBLE
                    binding.tiNewPassword.visibility = View.GONE
                    binding.tiConfirmNewPassword.visibility = View.GONE
                    binding.btnConfirmNewPassword.visibility = View.GONE
                    Snackbar.make(view, "Password update successful", Snackbar.LENGTH_SHORT).show()
                    vm.changePasswordStep.value = EStep.THREE
                } else {
                    vm.changePasswordStep.value = EStep.ONE
                    binding.tiOldPassword.visibility = View.VISIBLE
                    binding.btnConfirmOldPasswd.visibility = View.VISIBLE
                    binding.tvOne.visibility = View.VISIBLE

                    binding.ivCheck1.visibility = View.INVISIBLE
                    binding.tiNewPassword.visibility = View.GONE
                    binding.tiConfirmNewPassword.visibility = View.GONE
                    binding.btnConfirmNewPassword.visibility = View.GONE
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
                    binding.ivCheck2.visibility = View.INVISIBLE
                    binding.tvTwo.visibility = View.VISIBLE
                    binding.tiConfirmNewPassword.visibility = View.VISIBLE
                    binding.tiNewPassword.visibility = View.VISIBLE
                    binding.btnConfirmNewPassword.visibility = View.VISIBLE
                }
            }

        }
    }

}


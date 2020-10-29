package com.datepollsystems.datepoll.components.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.AppViewModel
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.databinding.FragmentFtueLoginBinding
import com.datepollsystems.datepoll.networkModule
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import timber.log.Timber


class FtueLoginFragment : Fragment() {

    private val ftueViewModel: FtueViewModel by sharedViewModel()

    private var _binding: FragmentFtueLoginBinding? = null
    val binding: FragmentFtueLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(networkModule)


        _binding = FragmentFtueLoginBinding.inflate(inflater, container, false)

        binding.vm = ftueViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.apply {
            tiUserName.editText?.addTextChangedListener {
                if (ftueViewModel.userName.value.isNullOrEmpty())
                    tiUserName.error = getString(R.string.invalid_username)
                else
                    tiUserName.isErrorEnabled = false
            }

            tiPassword.editText?.addTextChangedListener {
                if (ftueViewModel.password.value.isNullOrEmpty() || ftueViewModel.password.value?.length!! < 6)
                    tiPassword.error = getString(R.string.invalid_password)
                else
                    tiPassword.isErrorEnabled = false
            }

            btnLogin.setOnClickListener {

                if (!ftueViewModel.userName.value.isNullOrEmpty() && !ftueViewModel.password.value.isNullOrEmpty() &&
                    !tiUserName.isErrorEnabled && !tiPassword.isErrorEnabled
                ) {
                    Timber.i("Credentials are valid, performing login process")
                    loadKoinModules(networkModule)
                    ftueViewModel.login()

                } else {
                    Timber.i("Credentials are invalid")
                    Snackbar.make(it, "Your credentials are invalid", Snackbar.LENGTH_LONG).show()
                }

            }
        }


        setupObserver()

        return binding.root
    }

    private fun setupObserver() {
        ftueViewModel.apply {
            loginState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    when (it) {
                        ENetworkState.LOADING -> {
                            isLoading()
                        }
                        ENetworkState.DONE -> {
                            Timber.i("Login response successful")
                            isLoading(false)
                            findNavController().navigate(R.id.action_ftueLoginFragment_to_ftueSuccessfulFragment)
                        }
                        ENetworkState.ERROR -> {
                            Timber.e("Login not successful, maybe credentials are incorrect or internet connection is broken")
                            Timber.e("Error Code: ${it.code}")

                            if (it.code == 400 && it.messageCode == "change_password") {
                                findNavController().navigate(R.id.action_ftueLoginFragment_to_ftueFirstPasswordChange)
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.something_went_wrong),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                            isLoading(false)
                        }
                    }
                    loginState.postValue(null)
                }
            })
        }
    }

    private fun isLoading(v: Boolean = true) {
        if (v)
            binding.loading.visibility = View.VISIBLE
        else
            binding.loading.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        unloadKoinModules(networkModule)
    }
}
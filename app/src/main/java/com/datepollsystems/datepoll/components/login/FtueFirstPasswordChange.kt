package com.datepollsystems.datepoll.components.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentFtueFirstPasswordChangeBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FtueFirstPasswordChange : Fragment() {

    private val ftueViewModel: FtueViewModel by sharedViewModel()

    private var _binding: FragmentFtueFirstPasswordChangeBinding? = null
    val binding: FragmentFtueFirstPasswordChangeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFtueFirstPasswordChangeBinding.inflate(inflater, container, false)
        binding.vm = ftueViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        ftueViewModel.apply {
            firstPasswdState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    when(it) {
                        ENetworkState.LOADING -> isLoading()
                        ENetworkState.DONE -> {
                            isLoading(false)
                            findNavController().navigate(R.id.action_ftueFirstPasswordChange_to_ftueSuccessfulFragment)
                        }
                        ENetworkState.ERROR -> {
                            isLoading(false)
                            Snackbar.make(
                                binding.root,
                                getString(R.string.something_went_wrong),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
        }

        binding.apply {
            tiPassword.editText?.addTextChangedListener {
                if (ftueViewModel.firstChangePassword.value.isNullOrEmpty() || ftueViewModel.firstChangePassword.value?.length!! < 6)
                    tiPassword.error = getString(R.string.invalid_password)
                else
                    tiPassword.isErrorEnabled = false
            }
            btnSetPasswd.setOnClickListener {
                if (!tiPassword.isErrorEnabled &&
                    ftueViewModel.firstChangePassword.value == ftueViewModel.confirmFirstChangePassword.value) {
                    ftueViewModel.setFirstPasswd()
                } else {
                    Snackbar.make(
                        it,
                        getString(R.string.passwords_dont_match),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun isLoading(v: Boolean = true) {
        if (v)
            binding.loading.visibility = View.VISIBLE
        else
            binding.loading.visibility = View.INVISIBLE
    }
}
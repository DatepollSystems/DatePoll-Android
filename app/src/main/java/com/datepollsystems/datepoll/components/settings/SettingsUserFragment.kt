package com.datepollsystems.datepoll.components.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.UpdateUserRequest
import com.datepollsystems.datepoll.databinding.FragmentSettingsUserBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.utils.showMainSnack
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings_user.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsUserFragment : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()

    private var _binding: FragmentSettingsUserBinding? = null
    val binding: FragmentSettingsUserBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = DataBindingUtil.inflate<FragmentSettingsUserBinding>(
            inflater, R.layout.fragment_settings_user, container, false
        )

        binding.vm = vm
        binding.lifecycleOwner = this


        return binding.root
    }


    override fun onStart() {

        setObservers()

        vm.loadUserdata()

        userSettingsSwipeRefresh.isRefreshing = true
        userSettingsSwipeRefresh.setOnRefreshListener {
            userSettingsSwipeRefresh.isRefreshing = true
            vm.loadUserdata()
        }

        btnUpdateUser.setOnClickListener {
            vm.updateUser(
                UpdateUserRequest(
                    title = tiTitle.editText?.text.toString(),
                    firstname = tiFirstname.editText?.text.toString(),
                    surname = tiSurname.editText?.text.toString(),
                    birthday = tiBirthday.editText?.text.toString(),
                    location = tiLocation.editText?.text.toString(),
                    streetname = tiStreetName.editText?.text.toString(),
                    streetnumber = tiStreetNumber.editText?.text.toString(),
                    zipcode = tiZipCode.editText?.text.toString().toInt()
                )
            )
        }

        super.onStart()
    }

    private fun setObservers(){

        vm.updateUserState.observe(this, Observer {
            it?.let {
                when(it){
                    ENetworkState.LOADING -> {

                    }

                    ENetworkState.DONE -> {
                        userSettingsSwipeRefresh.isRefreshing = false
                        showMainSnack(
                            binding.root,
                            "Updated user successfully",
                            Snackbar.LENGTH_LONG
                        )
                        vm.updateUserState.postValue(null)
                        findNavController().popBackStack()
                    }

                    ENetworkState.ERROR -> {
                        userSettingsSwipeRefresh.isRefreshing = false
                        userSettingsLayout.visibility = View.VISIBLE
                        vm.updateUserState.postValue(null)
                        networkErrorOccurred()
                    }
                }
            }
        })

        vm.loadUserState.observe(this, Observer {
            it?.let {
                when(it){
                    ENetworkState.LOADING -> {

                    }

                    ENetworkState.DONE -> {
                        userSettingsSwipeRefresh.isRefreshing = false
                        userSettingsLayout.visibility = View.VISIBLE
                        vm.loadUserState.postValue(null)

                    }

                    ENetworkState.ERROR -> {
                        userSettingsSwipeRefresh.isRefreshing = false
                        networkErrorOccurred()
                        vm.loadUserState.postValue(null)
                    }
                }
            }
        })
    }

    private fun networkErrorOccurred(){
        showMainSnack(
            binding.root,
            getString(R.string.could_not_load_data),
            Snackbar.LENGTH_LONG
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.user_settings)
    }
}
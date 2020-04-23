package com.datepollsystems.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.AppObservableHandler
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.UpdateUserRequest
import com.datepollsystems.datepoll.databinding.FragmentSettingsUserBinding
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.vm.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings_user.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsUserFragment : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()
    val o: AppObservableHandler by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentSettingsUserBinding>(
            inflater, R.layout.fragment_settings_user, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this


        return view
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
                        o.showSnackbar.postValue("Updated user successfully")
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
        o.showSnackbar.postValue(context?.getString(R.string.could_not_load_data))
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.user_settings)
    }
}

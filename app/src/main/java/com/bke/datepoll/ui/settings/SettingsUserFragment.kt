package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bke.datepoll.AppObservableHandler
import com.bke.datepoll.R
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.databinding.FragmentSettingsUserBinding
import com.bke.datepoll.vm.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_settings.*
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
            o.progressBar.postValue(true)
            vm.updateUser(UpdateUserRequest(
                    title = tiTitle.editText?.text.toString(),
                    firstname = tiFirstname.editText?.text.toString(),
                    surname = tiSurname.editText?.text.toString(),
                    birthday = tiBirthday.editText?.text.toString(),
                    location = tiLocation.editText?.text.toString(),
                    streetname = tiStreetName.editText?.text.toString(),
                    streetnumber = tiStreetNumber.editText?.text.toString(),
                    zipcode = tiZipCode.editText?.text.toString().toInt()
            ))
        }

        super.onStart()
    }

    private fun setObservers(){
        o.progressBar.observe(this, Observer {
            if(it != null){
                if(it)
                    settingsProgressBar?.visibility = View.VISIBLE
                else
                    settingsProgressBar?.visibility = View.INVISIBLE

                o.progressBar.postValue(null)
            }
        })

        vm.userLoaded.observe(this, Observer {
            vm.user.addSource(it){u ->
                vm.user.value = u
            }
            userSettingsSwipeRefresh.isRefreshing = false
            userSettingsLayout.visibility = View.VISIBLE
        })

        vm.userUpdated.observe(this, Observer {
            if(it != null){
                if(it){
                    o.showSnackbar.postValue("Updated user successfully")
                    findNavController().popBackStack()
                } else {

                }
            }
        })
    }
}

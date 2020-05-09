package com.datepollsystems.datepoll.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.vm.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class LoadingFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

}

package com.bke.datepoll.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bke.datepoll.R
import com.bke.datepoll.vm.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoadingFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)

        return inflater.inflate(R.layout.fragment_loading, container, false)
    }
}

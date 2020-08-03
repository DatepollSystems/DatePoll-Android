package com.datepollsystems.datepoll.components.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentFtueServerInstanceBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_ftue_server_instance.*
import kotlinx.android.synthetic.main.fragment_ftue_server_instance.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FtueServerInstanceFragment : Fragment() {

    private var _binding: FragmentFtueServerInstanceBinding? = null
    private val binding get() = _binding!!

    val ftueViewModel: FtueViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFtueServerInstanceBinding.inflate(inflater, container, false)
        binding.vm = ftueViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupViewListeners()
        setupObserver()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FtueActivity.qrCodeRes) {
            if (resultCode == Activity.RESULT_OK) {
                val d = data?.getStringExtra(FtueActivity.qrCodeData)
                Timber.i("QrScan successfully")
                val dataObj = mapResultIntoObj(d)
                dataObj?.let {
                    val url = dataObj.url.removePrefix("https://")
                    Timber.i("url: $url")
                    ftueViewModel.serverInstanceUrl.postValue(url)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun mapResultIntoObj(s: String?): QRCodeData? {
        Timber.i("Try to map it into object")
        s?.let {
            val m = Moshi.Builder().build()
            val adapter = m.adapter(QRCodeData::class.java)
            return adapter.fromJson(s)
        }
        Timber.e("Result is not a datepoll qr code")
        return null
    }

    private fun setupViewListeners() {
        binding.apply {
            btnChoose.setOnClickListener {
                btnChoose.isEnabled = false
                ftueViewModel.loadInstances()
            }

            btnQRCodeScan.setOnClickListener {
                startActivityForResult(
                    Intent(requireActivity(), QrCodeScanActivity::class.java),
                    FtueActivity.qrCodeRes
                )
            }

            floatingActionButton.setOnClickListener {
                navigateToLoginFragment()
            }
        }
    }

    private fun navigateToLoginFragment() {
        val url = "https://${ftueViewModel.serverInstanceUrl.value}"
        if (ftueViewModel.validateServerInstance(url)) {
            Timber.i("Valid url: $url entered, continuing")
            ftueViewModel.setServer(url, 9230)
            tiServerDomain.error = ""
            findNavController().navigate(R.id.action_ftueServerInstanceFragment_to_ftueLoginFragment)
        } else {
            Timber.e("Invalid url")
            tiServerDomain.error = getString(R.string.url_incorrect)
        }
    }


    private fun setupObserver() {
        ftueViewModel.apply {
            instanceMenu.observe(viewLifecycleOwner, Observer {
                it?.let {
                    val bottomSheetFragment =
                        InstanceOptionsBottomSheetDialog(it, ftueViewModel.instanceClickResult)
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

                    ftueViewModel.instanceMenu.postValue(null)
                }
            })

            instanceMenuState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    when (it) {
                        ENetworkState.LOADING -> {
                            Timber.i("Loading instances")
                            binding.btnChoose.isEnabled = false
                            loading()
                        }
                        ENetworkState.DONE -> {
                            Timber.i("Loaded instances")
                            binding.btnChoose.isEnabled = true
                            loading(false)
                        }
                        ENetworkState.ERROR -> {
                            Timber.e("Loading instances failed")
                            binding.btnChoose.isEnabled = true
                            loading(false)
                        }
                    }
                }
            })

            instanceClickResult.observe(viewLifecycleOwner, Observer {
                it?.let {
                    ftueViewModel.serverInstanceUrl.postValue(it.appURL)
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        navigateToLoginFragment()
                    }
                    instanceClickResult.postValue(null)
                }
            })
        }
    }

    private fun loading(visible: Boolean = true) {
        if (visible)
            binding.root.loading.visibility = View.VISIBLE
        else
            binding.root.loading.visibility = View.INVISIBLE
    }
}
package com.datepollsystems.datepoll.components.settings


import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.datepollsystems.datepoll.R
import kotlinx.android.synthetic.main.fragment_settings_about.*


class SettingsAboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings_about, container, false)
    }

    override fun onStart() {
        val manager = activity?.packageManager
        val info = manager!!.getPackageInfo(requireActivity().packageName, PackageManager.GET_ACTIVITIES)
        val s = "${getString(R.string.version)} ${info.versionName}"
        tvVersion.text = s

        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.about)
    }

}

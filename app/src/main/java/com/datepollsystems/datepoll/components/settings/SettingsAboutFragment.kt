package com.datepollsystems.datepoll.components.settings


import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsAboutBinding


class SettingsAboutFragment : Fragment() {

    private var _binding: FragmentSettingsAboutBinding? = null
    val binding: FragmentSettingsAboutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsAboutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onStart() {
        val manager = activity?.packageManager
        val info = manager!!.getPackageInfo(requireActivity().packageName, PackageManager.GET_ACTIVITIES)
        val s = "${getString(R.string.version)} ${info.versionName}"
        binding.tvVersion.text = s
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.about)
    }
}

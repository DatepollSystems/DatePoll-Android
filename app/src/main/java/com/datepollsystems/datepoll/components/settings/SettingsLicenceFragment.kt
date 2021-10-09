package com.datepollsystems.datepoll.components.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsLicenceBinding
import timber.log.Timber


class SettingsLicenceFragment : Fragment() {

    lateinit var linkList: ArrayList<String>
    lateinit var nameList: ArrayList<String>

    private var _binding: FragmentSettingsLicenceBinding? = null
    val binding: FragmentSettingsLicenceBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsLicenceBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        val list = resources.getStringArray(R.array.licences).toList()

        linkList = getLinks(list)
        nameList = getNames(list)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, nameList)
        binding.lvLicences.adapter = adapter

        binding.lvLicences.setOnItemClickListener { _, _, position, _ ->
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(linkList[position]))
            startActivity(browserIntent)
        }
    }

    private fun getLinks(list: List<String>): ArrayList<String> {
        val newList = ArrayList<String>()
        for (i in list) {
            newList.add(i.substringBefore('|'))
        }

        Timber.i("newList $newList")
        return newList
    }

    private fun getNames(list: List<String>): ArrayList<String> {
        val newList = ArrayList<String>()
        for (i in list) {
            newList.add(i.substringAfter('|'))
        }
        Timber.i("newList $newList")
        return newList
    }
}
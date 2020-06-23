package com.datepollsystems.datepoll.ui.settings

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
import kotlinx.android.synthetic.main.fragment_settings_licence.*


class SettingsLicenceFragment : Fragment() {

    lateinit var linkList: ArrayList<String>
    lateinit var nameList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_licence, container, false)
    }


    override fun onStart() {
        super.onStart()

        val list = resources.getStringArray(R.array.licences).toList()

        linkList = getLinks(list)
        nameList = getNames(list)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, nameList);
        lvLicences.adapter = adapter

        lvLicences.setOnItemClickListener { _, _, position, _ ->
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

        Log.i("newList", newList.toString())
        return newList
    }

    private fun getNames(list: List<String>): ArrayList<String> {
        val newList = ArrayList<String>()
        for (i in list) {
            newList.add(i.substringAfter('|'))
        }
        Log.i("newList", newList.toString())
        return newList
    }
}
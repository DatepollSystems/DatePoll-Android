package com.bke.datepoll.ui.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.vm.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings_manage_sessions.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class SettingsManageSessionsFragment : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.manage_sessions)

        val v = inflater.inflate(R.layout.fragment_settings_manage_sessions, container, false)

        vm.loadSessions()

        val a = SessionsAdapter(vm)
        v.findViewById<RecyclerView>(R.id.sessions).adapter = a

        a.data = LinkedList()

        vm.sessions.value?.let {sessions
            a.data = LinkedList(it)
        }

        vm.sessions.observe(viewLifecycleOwner, Observer {
            a.data = LinkedList(it)
        })

        return v
    }


}

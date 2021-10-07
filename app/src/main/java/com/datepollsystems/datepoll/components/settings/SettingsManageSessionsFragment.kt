package com.datepollsystems.datepoll.components.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsAboutBinding
import com.datepollsystems.datepoll.databinding.FragmentSettingsManageSessionsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class SettingsManageSessionsFragment : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()

    private var _binding: FragmentSettingsManageSessionsBinding? = null
    val binding: FragmentSettingsManageSessionsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.manage_sessions)

        _binding = FragmentSettingsManageSessionsBinding.inflate(layoutInflater)

        vm.loadSessions()

        val a = SessionsAdapter(vm)
        binding.sessions.adapter = a

        a.data = LinkedList()

        vm.sessions.value?.let {
            a.data = LinkedList(it)
        }

        vm.sessions.observe(viewLifecycleOwner, Observer {
            a.data = LinkedList(it)
        })

        return binding.root
    }
}

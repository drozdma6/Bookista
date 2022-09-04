package com.exwise.bookista.fragment.books

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.exwise.bookista.BR
import com.exwise.bookista.R
import com.exwise.bookista.activity.LoginActivity
import com.exwise.bookista.adapter.SettingsAdapter
import com.exwise.bookista.dataBinding.setupDataBinding
import com.exwise.bookista.databinding.FragmentSettingsBinding
import com.exwise.bookista.domain.livedata.observer.SpecificEventObserver
import com.exwise.bookista.viewModel.home.SettingsFragmentViewModel
import com.exwise.bookista.viewModel.home.SimpleEvent
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment(R.layout.fragment_settings),
    SettingsAdapter.OnItemClickListener {

    private lateinit var dataBinding: FragmentSettingsBinding

    private val settingsFragmentViewModel: SettingsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = setupDataBinding(
            R.layout.fragment_settings,
            BR.viewmodel to settingsFragmentViewModel
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.bind()
    }

    override fun onResume() {
        super.onResume()

        observeLiveData()
    }

    private fun observeLiveData() {
        settingsFragmentViewModel.logOut.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                when (event) {
                    is SimpleEvent.Success -> {
                        openLoginActivity()
                    }
                    is SimpleEvent.Error -> {
                        Log.i("Daco zle sa stalo", "Joj")
                    }
                }
            }
        )
    }

    private fun openLoginActivity() {
        Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
        activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun FragmentSettingsBinding.bind() {
        val adapter = SettingsAdapter(this@SettingsFragment)
        settingsList.layoutManager = LinearLayoutManager(context)
        settingsList.adapter = adapter
        adapter.submitList(
            arrayListOf(
                Pair("Account", "Change Password, delete account"),
                Pair("Notifications", "Manage notifications"),
                Pair("Appearance", "Theme, Colors"),
                Pair("Privacy", "What do we collect"),
                Pair("Help and Support", "Contact us, FAQ"),
                Pair("About", "Who made this"),
                Pair("Log out", "Please do not do this")
            )
        )
    }

    override fun onItemClick(index: Int) {
        when (index) {
            6 -> {
                logOut()
            }
            else -> {

            }
        }
    }

    private fun logOut() {
        Log.i("ASda", "Asdasdasd")
        settingsFragmentViewModel.logOut()
    }
}
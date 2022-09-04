package com.exwise.bookista.fragment.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.exwise.bookista.R
import com.exwise.bookista.BR
import com.exwise.bookista.dataBinding.setupDataBinding
import com.exwise.bookista.domain.livedata.observer.SpecificEventObserver
import com.exwise.bookista.ext.hideSoftKeyboard
import com.exwise.bookista.ext.showAsSnackBar
import com.exwise.bookista.viewModel.authentication.ButtonPressedEvent
import com.exwise.bookista.viewModel.authentication.RegisterEvent
import com.exwise.bookista.viewModel.authentication.RegisterEventError
import com.exwise.bookista.viewModel.authentication.RegisterFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private val registerFragmentViewModel: RegisterFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding = setupDataBinding<ViewDataBinding>(
            R.layout.fragment_register,
            BR.viewmodel to registerFragmentViewModel
        )
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        observeLiveData()
    }

    private fun observeLiveData() {

        //Register Button
        registerFragmentViewModel.registerPressed.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                when (event) {
                    is ButtonPressedEvent.Pressed -> {
                        val view = view
                        val context = context
                        if (view != null && context != null) hideSoftKeyboard(context, view)
                    }
                }
            }
        )

        //Register
        registerFragmentViewModel.register.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let { view ->
                    when (event) {
                        is RegisterEvent.Success -> {
                            R.string.account_created.showAsSnackBar(view)
                            findNavController().popBackStack()
                        }
                        is RegisterEvent.Error -> {
                            when (event.error) {
                                is RegisterEventError.EmailAlreadyUsed -> R.string.email_already_used.showAsSnackBar(
                                    view
                                )
                                is RegisterEventError.InvalidEmail -> R.string.invalid_email.showAsSnackBar(
                                    view
                                )
                                is RegisterEventError.NetworkError -> R.string.network_error.showAsSnackBar(
                                    view
                                )
                                is RegisterEventError.PasswordsDoNotMatch -> R.string.passwords_do_not_match.showAsSnackBar(
                                    view
                                )
                                is RegisterEventError.TooManyRequests -> R.string.too_many_requests.showAsSnackBar(
                                    view
                                )
                                is RegisterEventError.UnknownError -> R.string.unknown_error.showAsSnackBar(
                                    view
                                )
                                is RegisterEventError.WeakPassword -> R.string.weak_password.showAsSnackBar(
                                    view
                                )
                            }
                        }
                    }
                }
            }
        )

        //Already have an account
        registerFragmentViewModel.alreadyHaveAnAccount.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                when (event) {
                    is ButtonPressedEvent.Pressed -> findNavController().popBackStack()
                }
            }
        )
    }
}
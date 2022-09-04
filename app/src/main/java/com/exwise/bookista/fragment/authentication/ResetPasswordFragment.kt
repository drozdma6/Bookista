package com.exwise.bookista.fragment.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.exwise.bookista.BR
import com.exwise.bookista.R
import com.exwise.bookista.dataBinding.setupDataBinding
import com.exwise.bookista.domain.livedata.observer.SpecificEventObserver
import com.exwise.bookista.ext.hideSoftKeyboard
import com.exwise.bookista.ext.showAsSnackBar
import com.exwise.bookista.viewModel.authentication.ButtonPressedEvent
import com.exwise.bookista.viewModel.authentication.RecoverAccountEvent
import com.exwise.bookista.viewModel.authentication.RecoverAccountEventError
import com.exwise.bookista.viewModel.authentication.ResetPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : Fragment() {

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val dataBinding = setupDataBinding<ViewDataBinding>(
            R.layout.fragment_reset_password,
            BR.viewmodel to resetPasswordViewModel
        )
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        observeLiveData()
    }

    private fun observeLiveData() {

        //Recover account Button
        resetPasswordViewModel.recoverAccountPressed.observe(
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

        //Recover account
        resetPasswordViewModel.recoverAccount.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let { view ->
                    when (event) {
                        is RecoverAccountEvent.Success -> {
                            R.string.recover_email_sent.showAsSnackBar(view)
                            findNavController().popBackStack()
                        }
                        is RecoverAccountEvent.Error -> {
                            when (event.error) {
                                is RecoverAccountEventError.InvalidEmail -> R.string.invalid_email.showAsSnackBar(
                                    view
                                )
                                is RecoverAccountEventError.NetworkError -> R.string.network_error.showAsSnackBar(
                                    view
                                )
                                is RecoverAccountEventError.TooManyRequests -> R.string.too_many_requests.showAsSnackBar(
                                    view
                                )
                                is RecoverAccountEventError.UnknownError -> R.string.unknown_error.showAsSnackBar(
                                    view
                                )
                                //Thrown when FirebaseAuthInvalidUserException
                                is RecoverAccountEventError.InvalidUser -> {
                                    R.string.recover_email_sent.showAsSnackBar(view)
                                    findNavController().popBackStack()
                                }
                            }
                        }
                    }
                }

            }
        )

        //Go back to Login
        resetPasswordViewModel.goBackToLogin.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let {
                    when (event) {
                        is ButtonPressedEvent.Pressed -> findNavController().popBackStack()
                    }
                }
            }
        )
    }
}
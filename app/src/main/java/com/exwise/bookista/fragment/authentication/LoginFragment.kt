package com.exwise.bookista.fragment.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.exwise.bookista.BR
import com.exwise.bookista.NavigationRouter
import com.exwise.bookista.R
import com.exwise.bookista.activity.MainScreenActivity
import com.exwise.bookista.dataBinding.setupDataBinding
import com.exwise.bookista.domain.livedata.observer.SpecificEventObserver
import com.exwise.bookista.ext.hideSoftKeyboard
import com.exwise.bookista.ext.showAsSnackBar
import com.exwise.bookista.viewModel.authentication.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val loginFragmentViewModel: LoginFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding = setupDataBinding<ViewDataBinding>(
            R.layout.fragment_login,
            BR.viewmodel to loginFragmentViewModel
        )
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        observeLiveData()
    }

    private fun observeLiveData() {
        //Login Button
        loginFragmentViewModel.loginPressed.observe(
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

        //Login
        loginFragmentViewModel.login.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let { view ->
                    when (event) {
                        is LoginEvent.Success -> {
                            switchActivities()
                        }
                        is LoginEvent.Error -> {
                            when (event.error) {
                                is LoginEventError.BadCredentials -> R.string.invalid_credentials.showAsSnackBar(
                                    view
                                )
                                is LoginEventError.InvalidEmail -> R.string.invalid_email.showAsSnackBar(
                                    view
                                )
                                is LoginEventError.NetworkError -> R.string.network_error.showAsSnackBar(
                                    view
                                )
                                is LoginEventError.TooManyRequests -> R.string.too_many_requests.showAsSnackBar(
                                    view
                                )
                                is LoginEventError.UnknownError -> R.string.unknown_error.showAsSnackBar(
                                    view
                                )
                                is LoginEventError.UserEmailNotVerified -> showEmailNotVerifiedSnackBar()
                            }
                        }
                    }
                }
            }
        )

        //Sign Up
        loginFragmentViewModel.signUpButton.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let { view ->
                    when (event) {
                        is ButtonPressedEvent.Pressed -> NavigationRouter(view).loginToRegister()
                    }
                }
            }
        )

        //Email verification
        loginFragmentViewModel.sendEmail.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let { view ->
                    when (event) {
                        is SendEmailEvent.Success -> R.string.verification_email_sent.showAsSnackBar(
                            view
                        )
                        is SendEmailEvent.Error -> {
                            when (event.error) {
                                is SendEmailError.NetworkError -> R.string.network_error.showAsSnackBar(
                                    view
                                )
                                is SendEmailError.TooManyRequests -> R.string.too_many_requests.showAsSnackBar(
                                    view
                                )
                                is SendEmailError.UnknownError -> R.string.unknown_error.showAsSnackBar(
                                    view
                                )
                            }
                        }
                    }
                }
            }
        )

        //Forgotten password
        loginFragmentViewModel.forgotPasswordButton.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let {
                    when (event) {
                        is ButtonPressedEvent.Pressed -> NavigationRouter(view).loginToResetPassword()
                    }
                }
            }
        )
    }

    private fun showEmailNotVerifiedSnackBar() {
        view?.let { view ->
            Snackbar.make(view, R.string.email_not_verified, Snackbar.LENGTH_LONG)
                .setAction(R.string.send) {
                    loginFragmentViewModel.sendVerificationEmail()
                }.show()
        }
    }

    private fun switchActivities() {
        Intent(activity, MainScreenActivity::class.java).apply {
            //back button wont take you back to login screen
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }
}
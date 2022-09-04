package com.exwise.bookista.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.exwise.bookista.R
import com.exwise.bookista.ext.showAsSnackBar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Bookista_NoActionBar)
        setContentView(R.layout.activity_login)

        checkWhetherActivityWasOpenWithFlags()
    }

    override fun onResume() {
        super.onResume()

        checkIfLoggedIn()
    }

    /**
     * Checks if Activity was opened with flag SHOW_TOAST_FLAG. If yes,
     * it shows given snackbar with message for the user
     */
    private fun checkWhetherActivityWasOpenWithFlags() {
        intent.extras?.getBoolean(SHOW_TOAST_FLAG)?.let { _ ->
            findViewById<View>(android.R.id.content).let {
                R.string.you_were_logged_out.showAsSnackBar(it)
            }
        }
    }

    /**
     * Checks if any user is logged in. If yes, it opens Main Activity
     */
    private fun checkIfLoggedIn() {
        FirebaseAuth.getInstance().currentUser?.let { _ ->
            goToMainActivity()
        }
    }

    /**
     * Opens Main Activity with flags, so pressing back button will not take user
     * back into Login Activity
     */
    private fun goToMainActivity() {
        Intent(this, MainScreenActivity::class.java).apply {
            //back button wont take you back to login screen
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.login_fragment).navigateUp()

    companion object {
        const val SHOW_TOAST_FLAG = "SHOW_TOAST_FLAG"
    }
}
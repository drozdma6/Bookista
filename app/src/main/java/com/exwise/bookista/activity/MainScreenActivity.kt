package com.exwise.bookista.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.exwise.bookista.R
import com.exwise.bookista.databinding.ActivityMainBinding
import com.exwise.bookista.domain.repository.AuthRepository
import com.exwise.bookista.fragment.books.BooksFragmentDirections
import com.exwise.bookista.generated.callback.OnClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MainScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val authRepository: AuthRepository by inject { parametersOf() }

    private val destinationIdsWithoutToolbar = setOf(
        R.id.fragment_book_detail
    )
    private val destinationIdsWithoutBottomBar = setOf(
        R.id.fragment_book_detail,
        R.id.fragment_bag
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.booksFragment,
                R.id.wishlistFragment,
                R.id.settingsFragment
            )
        )
        binding.toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isDestinationWithoutToolbar = destination.id in destinationIdsWithoutToolbar
            binding.toolbar.isVisible = !isDestinationWithoutToolbar

            val isDestinationWithoutBottomBar = destination.id in destinationIdsWithoutBottomBar
            binding.bottomNavigation.isVisible = !isDestinationWithoutBottomBar
        }

    }


    override fun onResume() {
        super.onResume()

        checkIfUserIsLoggedIn()
    }

    /**
     * Sometimes Firebase logs user out, or if account is disabled or password changed etc
     */
    private fun checkIfUserIsLoggedIn() {
        if(!authRepository.isUserLoggedIn()) {
            openLoginActivity()
        }
    }

    /**
     * Opens Login Activity with flags, so pressing back button will not take user
     * back into Main Activity
     */
    private fun openLoginActivity() {
        Intent(applicationContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(LoginActivity.SHOW_TOAST_FLAG, true)
            startActivity(this)
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
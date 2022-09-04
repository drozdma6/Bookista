package com.exwise.bookista

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.exwise.bookista.data.Book
import com.exwise.bookista.fragment.authentication.LoginFragmentDirections

import com.exwise.bookista.fragment.books.BooksFragmentDirections

class NavigationRouter(private val view: View?) {

    fun loginToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        openAction(action)
    }

    fun loginToResetPassword() {
        val action = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
        openAction(action)
    }

    fun booksToBag() {
        val action = BooksFragmentDirections.toBagFragment()
        openAction(action)
    }

    fun booksToDetail(book: Book) {
        val action = BooksFragmentDirections.toBookDetailFragment(book)
        openAction(action)
    }


    private fun openAction(action: NavDirections) {
        view?.let {
            val navigationController = Navigation.findNavController(view)
            navigationController.navigate(action)
        }
    }
}

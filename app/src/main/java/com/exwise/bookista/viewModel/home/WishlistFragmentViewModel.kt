package com.exwise.bookista.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exwise.bookista.data.Book
import com.exwise.bookista.domain.repository.FirebaseBooksRepository
import com.exwise.bookista.domain.usecase.BookFromQuerySnapshotUseCase
import com.exwise.bookista.domain.usecase.BookSwitchInWishlistBooleanUseCase

class WishlistFragmentViewModel(
    private val booksRepository: FirebaseBooksRepository,
    private val booksSwitchInWishlistBooleanUseCase: BookSwitchInWishlistBooleanUseCase,
    private val bookFromQuerySnapshotUseCase: BookFromQuerySnapshotUseCase
): ViewModel() {

    private val _removedFromWishlist: MutableLiveData<SimpleEvent> =
        MutableLiveData<SimpleEvent>()
    val removedFromWishlist: LiveData<SimpleEvent> = _removedFromWishlist

    private val _loadedBooks: MutableLiveData<LoadBooksEvent> =
        MutableLiveData<LoadBooksEvent>()
    val loadedBooks: LiveData<LoadBooksEvent> = _loadedBooks

    fun loadBooksInWishlist() {
        booksRepository.getBooks { querySnapshot, error ->
            error?.let {
                _loadedBooks.postValue(LoadBooksEvent.Error())
            }
            querySnapshot?.let {
                val books = ArrayList<Book>()
                for (documentSnapshot in it) {
                    val book = bookFromQuerySnapshotUseCase.getBook(documentSnapshot)
                    if(book.inWishlist == true) books.add(book)
                }
                _loadedBooks.postValue(LoadBooksEvent.Success(books))
            }
        }
    }

    fun removeBookFromWishlist(book: Book) {
        booksSwitchInWishlistBooleanUseCase.switchWishlist(book,
            {
                _removedFromWishlist.postValue(SimpleEvent.Success())
            },
            {
                it.printStackTrace()
                _removedFromWishlist.postValue(SimpleEvent.Error())
            })
    }
}
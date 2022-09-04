package com.exwise.bookista.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exwise.bookista.data.Book
import com.exwise.bookista.domain.livedata.model.Event
import com.exwise.bookista.domain.repository.FirebaseBooksRepository
import com.exwise.bookista.domain.usecase.BookFromQuerySnapshotUseCase
import com.exwise.bookista.domain.usecase.BookSwitchInWishlistBooleanUseCase
import com.exwise.bookista.domain.usecase.BooksAddToBagUseCase

class BooksFragmentViewModel(
    private val booksRepository: FirebaseBooksRepository,
    private val bookFromQuerySnapshotUseCase: BookFromQuerySnapshotUseCase,
    private val booksSwitchInWishlistBooleanUseCase: BookSwitchInWishlistBooleanUseCase,
    private val booksAddToBagUseCase: BooksAddToBagUseCase
) : ViewModel() {

    private val _loadedBooks: MutableLiveData<LoadBooksEvent> =
        MutableLiveData<LoadBooksEvent>()
    val loadedBooks: LiveData<LoadBooksEvent> = _loadedBooks

    private val _switchWishlist: MutableLiveData<SimpleEvent> =
        MutableLiveData<SimpleEvent>()
    val switchWishList: LiveData<SimpleEvent> = _switchWishlist

    private val _addToBag: MutableLiveData<AddToBagEvent> =
        MutableLiveData<AddToBagEvent>()
    val addToBag: LiveData<AddToBagEvent> = _addToBag

    fun getBooks() {
        booksRepository.getBooks { querySnapshot, error ->
            error?.let {
                _loadedBooks.postValue(LoadBooksEvent.Error())
            }
            querySnapshot?.let {
                val books = ArrayList<Book>()
                for (documentSnapshot in it) {
                    books.add(bookFromQuerySnapshotUseCase.getBook(documentSnapshot))
                }
                _loadedBooks.postValue(LoadBooksEvent.Success(books))
            }
        }
    }

    fun switchWishlistBook(book: Book) {
        booksSwitchInWishlistBooleanUseCase.switchWishlist(book,
            {
                _switchWishlist.postValue(SimpleEvent.Success())
            },
            {
                it.printStackTrace()
                _switchWishlist.postValue(SimpleEvent.Error())
            })
    }

    fun addToBag(book: Book) {
        if (book.inABag == true) {
            _addToBag.postValue(AddToBagEvent.AlreadyInABag())
        } else {
            booksAddToBagUseCase.addToBag(book,
                {
                    _addToBag.postValue(AddToBagEvent.Success())
                },
                {
                    it.printStackTrace()
                    _addToBag.postValue(AddToBagEvent.Error())
                })
        }
    }
}

sealed class LoadBooksEvent : Event() {
    class Success(val books: ArrayList<Book>) : LoadBooksEvent()
    class Error : LoadBooksEvent()
}

sealed class AddToBagEvent : Event() {
    class Success : AddToBagEvent()
    class AlreadyInABag : AddToBagEvent()
    class Error : AddToBagEvent()
}

sealed class SimpleEvent : Event() {
    class Success : SimpleEvent()
    class Error : SimpleEvent()
}
package com.exwise.bookista.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exwise.bookista.data.Book
import com.exwise.bookista.domain.repository.FirebaseBooksRepository
import com.exwise.bookista.domain.usecase.BookFromQuerySnapshotUseCase
import com.exwise.bookista.domain.usecase.BooksRemoveFromBagUseCase
import com.exwise.bookista.viewModel.home.LoadBooksEvent
import com.exwise.bookista.viewModel.home.SimpleEvent

class BagFragmentViewModel(
    private val booksRepository: FirebaseBooksRepository,
    private val booksRemoveFromBagUseCase: BooksRemoveFromBagUseCase,
    private val bookFromQuerySnapshotUseCase: BookFromQuerySnapshotUseCase
) : ViewModel() {

    private val _removedFromBag: MutableLiveData<SimpleEvent> =
        MutableLiveData<SimpleEvent>()
    val removedFromBag: LiveData<SimpleEvent> = _removedFromBag

    private val _loadedBooks: MutableLiveData<LoadBooksEvent> =
        MutableLiveData<LoadBooksEvent>()
    val loadedBooks: LiveData<LoadBooksEvent> = _loadedBooks

    fun loadBooksInBag() {
        booksRepository.getBooks { querySnapshot, error ->
            error?.let {
                _loadedBooks.postValue(LoadBooksEvent.Error())
            }
            querySnapshot?.let {
                val books = ArrayList<Book>()
                for (documentSnapshot in it) {
                    val book = bookFromQuerySnapshotUseCase.getBook(documentSnapshot)
                    if(book.inABag == true) books.add(book)
                }
                _loadedBooks.postValue(LoadBooksEvent.Success(books))
            }
        }
    }

    fun removeFromBag(book: Book) {
        booksRemoveFromBagUseCase.removeFromBag(book,
            {
                _removedFromBag.postValue(SimpleEvent.Success())
            },
            {
                it.printStackTrace()
                _removedFromBag.postValue(SimpleEvent.Error())
            })
    }


}
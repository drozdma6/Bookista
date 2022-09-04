package com.exwise.bookista.fragment.books

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.exwise.bookista.BR
import com.exwise.bookista.NavigationRouter
import com.exwise.bookista.R
import com.exwise.bookista.adapter.BooksAdapter
import com.exwise.bookista.data.Book
import com.exwise.bookista.dataBinding.setupDataBinding
import com.exwise.bookista.databinding.FragmentBooksBinding
import com.exwise.bookista.domain.livedata.observer.SpecificEventObserver
import com.exwise.bookista.ext.showAsSnackBar
import com.exwise.bookista.viewModel.home.AddToBagEvent
import com.exwise.bookista.viewModel.home.BooksFragmentViewModel
import com.exwise.bookista.viewModel.home.LoadBooksEvent
import com.exwise.bookista.viewModel.home.SimpleEvent
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class BooksFragment : Fragment(R.layout.fragment_books),
    BooksAdapter.OnWishlistListener,
    BooksAdapter.OnBookListener,
    BooksAdapter.OnBagListener {

    private val viewModel: BooksFragmentViewModel by viewModel()

    private lateinit var dataBinding: FragmentBooksBinding

    private lateinit var adapter: BooksAdapter

    private var text: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = setupDataBinding(
            R.layout.fragment_books,
            BR.viewmodel to viewModel
        )
        dataBinding.toBag.setOnClickListener {
            NavigationRouter(it).booksToBag()
        }
        dataBinding.searchBar.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText != "") {
                    text = newText.lowercase(Locale.getDefault())
                }else{
                    text = null
                }
                viewModel.getBooks()
                return false
            }
        })
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.bind()
    }

    override fun onResume() {
        super.onResume()
        observeLiveData()
        viewModel.getBooks()
    }

    private fun observeLiveData() {
        viewModel.loadedBooks.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                when (event) {
                    is LoadBooksEvent.Error -> {
                        view?.let {
                            R.string.unknown_error.showAsSnackBar(it)
                        }
                    }
                    is LoadBooksEvent.Success -> {
                        if(text != null){
                            adapter.submitList(event.books.filter{book ->
                                book.name!!.lowercase(Locale.getDefault()).contains(text.toString()) ||
                                book.author!!.lowercase(Locale.getDefault()).contains(text.toString())
                            })
                        } else{
                            adapter.submitList(event.books)
                        }
                    }
                }
            }
        )

        viewModel.switchWishList.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                when (event) {
                    is SimpleEvent.Success -> {}
                    is SimpleEvent.Error -> {
                        view?.let {
                            R.string.unknown_error.showAsSnackBar(it)
                        }
                    }
                }
            }
        )

        viewModel.addToBag.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                view?.let {
                    when (event) {
                        is AddToBagEvent.AlreadyInABag -> {
                            showRaisedSnackbar(it, R.string.book_already_in_bag)
                        }
                        is AddToBagEvent.Error -> {
                            showRaisedSnackbar(it, R.string.unknown_error)
                        }
                        is AddToBagEvent.Success -> {
                            showRaisedSnackbar(it, R.string.book_added_to_bag)
                        }
                    }
                }
            }
        )
    }

    private fun showRaisedSnackbar(it: View, resource: Int) {
        val snackbar = Snackbar.make(it, resource, Snackbar.LENGTH_LONG)
        context?.let {
            snackbar.view.translationY = (-(convertDpToPixel(50F, it)))
        }
        snackbar.show()
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun FragmentBooksBinding.bind() {
        adapter = BooksAdapter(this@BooksFragment, this@BooksFragment, this@BooksFragment)
        booksRecyclerView.layoutManager = LinearLayoutManager(context)
        booksRecyclerView.adapter = adapter
    }

    override fun navigateToDetail(book: Book) {
        view?.let {
            NavigationRouter(it).booksToDetail(book)
        }
    }

    override fun onWishlistClick(book: Book) {
        viewModel.switchWishlistBook(book)
    }

    override fun addToBag(book: Book) {
        viewModel.addToBag(book)
    }
}
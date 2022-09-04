package com.exwise.bookista.fragment.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.exwise.bookista.BR
import com.exwise.bookista.R
import com.exwise.bookista.adapter.WishlistAdapter
import com.exwise.bookista.data.Book
import com.exwise.bookista.dataBinding.setupDataBinding
import com.exwise.bookista.databinding.FragmentWishListBinding
import com.exwise.bookista.domain.livedata.observer.SpecificEventObserver
import com.exwise.bookista.ext.showAsSnackBar
import com.exwise.bookista.viewModel.home.LoadBooksEvent
import com.exwise.bookista.viewModel.home.WishlistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishListFragment : Fragment(), WishlistAdapter.OnRemoveClickListener {

    private val viewModel: WishlistFragmentViewModel by viewModel()

    private lateinit var dataBinding: FragmentWishListBinding

    private lateinit var adapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = setupDataBinding(
            R.layout.fragment_wish_list,
            BR.viewmodel to viewModel
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.bind()
    }

    override fun onResume() {
        super.onResume()

        observeLiveData()
        viewModel.loadBooksInWishlist()
    }

    private fun FragmentWishListBinding.bind() {
        adapter = WishlistAdapter(this@WishListFragment)
        booksRecyclerView.layoutManager = LinearLayoutManager(context)
        booksRecyclerView.adapter = adapter
    }

    private fun observeLiveData() {
        viewModel.loadedBooks.observe(
            viewLifecycleOwner,
            SpecificEventObserver { event ->
                when(event) {
                    is LoadBooksEvent.Error -> {
                        view?.let {
                            R.string.unknown_error.showAsSnackBar(it)
                        }
                    }
                    is LoadBooksEvent.Success -> {
                        adapter.submitList(event.books)
                    }
                }
            }
        )
    }

    override fun onRemoveClick(book: Book) {
        viewModel.removeBookFromWishlist(book)
    }
}

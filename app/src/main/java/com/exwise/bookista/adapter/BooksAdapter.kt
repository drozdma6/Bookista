package com.exwise.bookista.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exwise.bookista.GlideApp
import com.exwise.bookista.R
import com.exwise.bookista.data.Book
import com.exwise.bookista.databinding.ItemBookBinding
import com.google.firebase.storage.FirebaseStorage

class BooksAdapter(
    private val onBookClick: OnBookListener,
    private val onWishlistClick: OnWishlistListener,
    private val onBagListener: OnBagListener
) : ListAdapter<Book, BooksAdapter.BookHolder>(BooksDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return BookHolder(binding, onBookClick, onWishlistClick, onBagListener)
    }

    override fun onBindViewHolder(bookHolder: BookHolder, position: Int) {
        val book = getItem(position)
        bookHolder.bind(book)
    }

    class BookHolder(
        private val binding: ItemBookBinding,
        private val onBookClick: OnBookListener,
        private val onWishlistClick: OnWishlistListener,
        private val onBagListener: OnBagListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.txtName.text = book.name
            binding.txtAuthor.text = book.author
            binding.root.setOnClickListener { onBookClick.navigateToDetail(book) }
            if (book.inWishlist == true) {
                binding.hearthIcon.setImageResource(R.drawable.ic_heart_selected)
            } else {
                binding.hearthIcon.setImageResource(R.drawable.ic_heart)
            }

            binding.hearthIcon.setOnClickListener {
                onWishlistClick.onWishlistClick(book)
            }

            binding.addToBag.setOnClickListener {
                onBagListener.addToBag(book)
            }
            bindImage(book)
        }

        private fun bindImage(book: Book) {
            if (book.imageURI.isNullOrBlank()) {
                binding.imgBook.setImageResource(R.drawable.ic_baseline_cloud_download_24)
            } else {
                GlideApp.with(itemView)
                    .load(
                        FirebaseStorage.getInstance().getReferenceFromUrl(book.imageURI.toString())
                    )
                    .placeholder(R.color.ButtonEnabled)
                    .into(binding.imgBook)
            }
        }
    }

    class BooksDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    interface OnWishlistListener {
        fun onWishlistClick(book: Book)
    }

    interface OnBookListener {
        fun navigateToDetail(book: Book)
    }

    interface OnBagListener {
        fun addToBag(book: Book)
    }
}